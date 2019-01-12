package com.symphony.bkash;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.symphony.bkash.data.model.UpdateResponse;
import com.symphony.bkash.data.model.PostInfo;
import com.symphony.bkash.data.model.PostResponse;
import com.symphony.bkash.data.remote.TokenDataApiService;
import com.symphony.bkash.data.remote.TokenDataApiUtils;
import com.symphony.bkash.util.ConnectionUtils;
import com.symphony.bkash.util.Constant;
import com.symphony.bkash.util.SharedPrefUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.symphony.bkash.util.Constant.ACTIVATION_KEY;
import static com.symphony.bkash.util.Constant.INFO_ID_KEY;
import static com.symphony.bkash.util.Constant.PACKAGE_NAME_LAUNCHING_APP;

public class UploaderJobService extends JobService {

    private final static String TAG = "UploaderJobService";
    private boolean jobCancelled = false;
    public static final String SIM_Number = "23382346";
    private TokenDataApiService tokenDataAPIService = TokenDataApiUtils.getUserDataAPIServices();

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");
        doBackgroundJob(params, getApplicationContext());
        return true;
    }

    private void doBackgroundJob(final JobParameters params, final Context ctx){
        new Thread(new Runnable() {
            @Override
            public void run() {

                //IMEI1, IMEI2, MAC, AndroidID, SIM1, SIM2, Model, Activated

                Log.d(TAG, "run: in thread");
                //Sim number
                String sim1 = "000000", sim2 = "000000";
                List<String> simList = ConnectionUtils.getSimNumber(ctx);
                if(simList != null){
                    if(simList.size() == 1){
                        sim1 = simList.get(0);
                    } else if(simList.size() == 2){
                        sim1 = simList.get(0);
                        sim2 = simList.get(1);
                    }
                }
                String mac = "00:00:00:00:00";
                String modelPref = "Symphony ", model;
                model = ConnectionUtils.getSystemProperty("ro.product.device");
                if(model == null || model.isEmpty()){
                    model = ConnectionUtils.getSystemProperty("ro.build.product");
                }
                TelephonyManager telemamanger = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
                if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                    String imei1 = telemamanger.getImei(0);
                    String imei2 = telemamanger.getImei(1);
                    if(imei1.isEmpty()){
                        imei1 = "123456987565";
                        imei2 = "1234567896589";
                    }
                    mac = ConnectionUtils.getMAC();
                    String android_id = Settings.Secure.getString(ctx.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    long info_id = SharedPrefUtils.getLongPreference(ctx, INFO_ID_KEY, 0);
                    if(info_id == 0) {
                        sendInfo(ctx, imei1, imei2, mac, android_id, sim1, sim2, getActivation(ctx), modelPref + model, params);
                    } else {
                        updateInfo(info_id, imei1, imei2, mac, android_id, sim1, sim2, getActivation(ctx), modelPref + model, params);
                    }
                } else {
                    jobFinished(params, false);
                }


            }
        }).start();
    }


    public void sendInfo(final Context ctx, final String imei1, String imei2, String mac, String android_id, String sim1, String sim2, String activated, String model, final JobParameters params){
        tokenDataAPIService.saveInfo(imei1, imei2, mac, android_id, sim1, sim2, activated, model).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.body().getCode() == 200) {
                    SharedPrefUtils.setLongPreference(ctx, INFO_ID_KEY, response.body().getId());
                    Log.d(TAG, "SUCCESS POST: Job finished");

                }else if(response.body().getCode() == 204) {
                    SharedPrefUtils.setLongPreference(ctx, INFO_ID_KEY, response.body().getId());
                    Log.d(TAG, "SUCCESS POST:");
                }
                getActivationFromServer(ctx, imei1, params);
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d(TAG, "FAILED POST: Job finished");
                getActivationFromServer(ctx, imei1, params);

            }
        });
    }

    public void updateInfo(long id, String imei1,String imei2,String mac, String android_id, String sim1, String sim2, String activated, String model, final JobParameters params){
        PostInfo postInfo = new PostInfo(imei1,imei2,mac,android_id,sim1,sim2, activated,model);
        tokenDataAPIService.updateInfo(id, postInfo).enqueue(new Callback<UpdateResponse>() {
            @Override
            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {

                if(response.body().getCode().equals("200")){
                    Log.d(TAG, "SUCCESS UPDATE: Job finished");
                }
                jobFinished(params, false);
            }

            @Override
            public void onFailure(Call<UpdateResponse> call, Throwable t) {
                Log.d(TAG, "FAILED UPDATE: Job finished");
                jobFinished(params, false);
            }
        });
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "job cancelled before completion");
        jobCancelled = true;
        return true;
    }

    public String getActivation(Context ctx){
        // activation algorithm
        //0 - Not Acivated
        //1 - Activated
        //2 - Independent Install
        //3 - App Removed
        boolean isInstalled = ConnectionUtils.isPackageInstalled(PACKAGE_NAME_LAUNCHING_APP,ctx.getPackageManager());
        int prevActivated = SharedPrefUtils.getIntegerPreference(ctx, ACTIVATION_KEY, -1);
        int activated = 0;
        if(isInstalled){
            switch (prevActivated){
                case -1:
                    activated = 2;
                    break;
                case 0:
                    activated = 1;
                    break;
                case 1:
                    activated = 1;
                    break;
                case 2:
                    activated = 2;
                    break;
                case 3:
                    activated = 1;
                    break;
                default:
                    break;
            }
        } else{
            switch (prevActivated){
                case -1:
                    activated = 0;
                    break;
                case 0:
                    activated = 0;
                    break;
                case 1:
                    activated = 3;
                    break;
                case 2:
                    activated = 0;
                    break;
                case 3:
                    activated = 3;
                    break;
                default:
                    break;
            }
        }
        return String.valueOf(activated);
    }

    public void getActivationFromServer(final Context ctx, String imei1, final JobParameters params){
        tokenDataAPIService.getInfo(imei1).enqueue(new Callback<PostInfo>(){
            @Override
            public void onResponse(Call<PostInfo> call, Response<PostInfo> response) {
                int activation = 0;
                if(null != response.body().getActivated() && !TextUtils.isEmpty(response.body().getActivated())){

                    switch (response.body().getActivated()){
                        case "Not Activated":
                            activation = 0;
                            break;
                        case "Activated":
                            activation = 1;
                            break;
                        case "Independent Install ":
                            activation = 2;
                            break;
                        case "App Remove":
                            activation = 3;
                            break;
                            default:
                                break;
                    }
                }
                SharedPrefUtils.setIntegerPreference(ctx, ACTIVATION_KEY, activation);
                jobFinished(params, false);
            }

            @Override
            public void onFailure(Call<PostInfo> call, Throwable t) {
                Log.d(TAG, "FAILED UPDATE: Job finished");
                jobFinished(params, false);
            }
        });
    }
}
