package com.symphony.bkashg;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import androidx.core.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.symphony.bkashg.data.model.PostResponseEdisonBokachoda;
import com.symphony.bkashg.data.model.UpdateResponse;
import com.symphony.bkashg.data.model.PostInfo;
import com.symphony.bkashg.data.model.PostResponse;
import com.symphony.bkashg.data.remote.TokenDataApiService;
import com.symphony.bkashg.data.remote.TokenDataApiUtils;
import com.symphony.bkashg.util.ConnectionUtils;
import com.symphony.bkashg.util.SharedPrefUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.symphony.bkashg.util.Constant.ACTIVATION_KEY;
import static com.symphony.bkashg.util.Constant.EDISON_INFO_IMEI;
import static com.symphony.bkashg.util.Constant.INFO_ID_KEY;
import static com.symphony.bkashg.util.Constant.LOCAL_INFO_ID_KEY;
import static com.symphony.bkashg.util.Constant.PACKAGE_NAME_LAUNCHING_APP;

public class UploaderJobService extends JobService {

    private final static String TAG = "UploaderJobService";
    private final static String TAGE = "EDISON BOKACHODA";
    public final String token = "Bearer Ymthc2hlZGE6Ykthc2ghc3ltcGhvbnkkMTIz";
    private boolean jobCancelled = false;
    public static final String SIM_Number = "23382346";
    private String brand = "";
    private TokenDataApiService tokenDataAPIService = TokenDataApiUtils.getUserDataAPIServices();

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");
        doBackgroundJob(params, getApplicationContext());
        return true;
    }

    private void doBackgroundJob(final JobParameters params, final Context ctx){
                //IMEI1, IMEI2, MAC, AndroidID, SIM1, SIM2, Model, Activated
                //Sim number
        new Thread(new Runnable() {
            public void run() {
                String sim1 = "No SIM", sim2 = "No SIM";
                List<String> simList = ConnectionUtils.getSimNumber(ctx);
                if (simList != null) {
                    if (simList.size() == 1) {
                        sim1 = simList.get(0);
                    } else if (simList.size() == 2) {
                        sim1 = simList.get(0);
                        sim2 = simList.get(1);
                    }
                }
                String mac = "00:00:00:00:00";
                brand = Build.BRAND + " ";
                String modelPref = brand, model;
                model = ConnectionUtils.getSystemProperty("ro.product.device");
                if (model == null || model.isEmpty()) {
                    model = ConnectionUtils.getSystemProperty("ro.build.product");
                }
                TelephonyManager telemamanger = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
                if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                    String imei1 = telemamanger.getImei(0);
                    String imei2 = telemamanger.getImei(1);
                    if (null == imei1 || TextUtils.isEmpty(imei1)) {
                        imei1 = "123456987565";
                        imei2 = "1234567896589";
                    }
                    mac = ConnectionUtils.getMAC();
                    String android_id = Settings.Secure.getString(ctx.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    long info_id = SharedPrefUtils.getLongPreference(ctx, INFO_ID_KEY, 0);

                    if (info_id == 0) {
                        sendInfo(ctx, imei1, imei2, mac, android_id, sim1, sim2, getActivation(ctx), modelPref + model, params);
                    } else {
                        updateInfo(ctx, info_id, imei1, imei2, mac, android_id, sim1, sim2, getActivation(ctx), modelPref + model, params);
                    }
                } else {
                    jobFinished(params, true);
                }

            }
        }).start();;
    }


    public void sendInfo(final Context ctx, final String imei1, final String imei2, final String mac, final String android_id, final String sim1, final String sim2, final String activated, final String model, final JobParameters params){
        Log.d(TAG, "sendInfo: active status: "+ activated);
         SharedPrefUtils.setIntegerPreference(ctx, ACTIVATION_KEY, Integer.valueOf(activated));
        tokenDataAPIService.saveInfo(token, imei1, imei2, mac, android_id, sim1, sim2, activated, model).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.body().getCode() == 200) {
                    SharedPrefUtils.setLongPreference(ctx, INFO_ID_KEY, response.body().getId());
                    Log.d(TAG, "SUCCESS POST: Job finished");
                }else if(response.body().getCode() == 204) {
                    SharedPrefUtils.setLongPreference(ctx, INFO_ID_KEY, response.body().getId());
                    Log.d(TAG, "SUCCESS POST:");
                }
                sendInfoLocal(ctx, imei1, imei2, mac, android_id, sim1, sim2, activated, model, params);
                sendInfoEdison(ctx, imei1, imei2, mac, android_id, sim1, sim2, activated, model, params);
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d(TAG, "FAILED POST: Job finished");
                //getActivationFromServer(ctx, imei1, params);
                //jobFinished(params, false);
                sendInfoLocal(ctx, imei1, imei2, mac, android_id, sim1, sim2, activated, model, params);
                sendInfoEdison(ctx, imei1, imei2, mac, android_id, sim1, sim2, activated, model, params);
            }
        });
    }

    public void  sendInfoLocal(final Context ctx, final String imei1, String imei2, String mac, String android_id, String sim1, String sim2, final String activated, String model, final JobParameters params){
        Log.d(TAG, "sendInfo: active status: "+ activated);
        //SharedPrefUtils.setIntegerPreference(ctx, ACTIVATION_KEY, Integer.valueOf(activated));
        PostInfo postInfo = new PostInfo(imei1,imei2,mac,android_id,sim1,sim2, activated,model);
        long info_id = SharedPrefUtils.getLongPreference(ctx, LOCAL_INFO_ID_KEY, 0);
        if(info_id == 0) {
            String url = "https://bkash.gonona-lab.com/api/bKashStore";
            tokenDataAPIService.saveInfoLocal(url, imei1, imei2, mac, android_id, sim1, sim2, activated, model).enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                    if (response.body().getCode() == 200) {
                        SharedPrefUtils.setLongPreference(ctx, LOCAL_INFO_ID_KEY, response.body().getId());
                        Log.d(TAG, "SUCCESS POST: Job finished");
                    } else if (response.body().getCode() == 204) {
                        SharedPrefUtils.setLongPreference(ctx, LOCAL_INFO_ID_KEY, response.body().getId());
                        Log.d(TAG, "SUCCESS POST:");
                    }
                    jobFinished(params, false);
                }

                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {
                    Log.d(TAG, "FAILED POST: Job finished");
                    //getActivationFromServer(ctx, imei1, params);
                    jobFinished(params, false);


                }
            });
        } else {
            updateInfoLocal(ctx, postInfo, params);
        }
    }

    public void updateInfo(final Context ctx, final long id, final String imei1, final String imei2, final String mac, final String android_id, final String sim1, final String sim2, final String activated, final String model, final JobParameters params){
        final PostInfo postInfo = new PostInfo(imei1,imei2,mac,android_id,sim1,sim2, activated,model);
        Log.d(TAG, "updateInfo: active status: "+ activated);
        SharedPrefUtils.setIntegerPreference(ctx, ACTIVATION_KEY, Integer.valueOf(activated));
        tokenDataAPIService.updateInfo(token, id, postInfo).enqueue(new Callback<UpdateResponse>() {
            @Override
            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {

                if(response.body().getCode().equals("200")){
                    Log.d(TAG, "SUCCESS UPDATE: Job finished");
                }
                updateInfoLocal(ctx, postInfo, params);
                updateInfoEdison(ctx, postInfo, params);
            }

            @Override
            public void onFailure(Call<UpdateResponse> call, Throwable t) {
                Log.d(TAG, "FAILED UPDATE: Job finished");
                //jobFinished(params, false);
                updateInfoLocal(ctx, postInfo, params);
            }
        });
    }

    public void updateInfoLocal(Context ctx, final PostInfo postInfo, final JobParameters params){


        long info_id = SharedPrefUtils.getLongPreference(ctx, LOCAL_INFO_ID_KEY, 0);
        String url = "https://bkash.gonona-lab.com/api/bKashUpdate/"+String.valueOf(info_id);

        tokenDataAPIService.updateInfoLocal(url, postInfo).enqueue(new Callback<UpdateResponse>() {
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
                    activated = 3;
                    break;
                case 3:
                    activated = 3;
                    break;
                default:
                    break;
            }
        }

        //SharedPrefUtils.setIntegerPreference(ctx, ACTIVATION_KEY, activated);
        return String.valueOf(activated);
    }

   /* public void getActivationFromServer(final Context ctx, String imei1, final JobParameters params){
        tokenDataAPIService.getInfo(imei1).enqueue(new Callback<InfoResponse>(){
            @Override
            public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
                int activation = 0;
                if(null != response.body().getDataList()){
                    activation = response.body().getDataList().getActivated();
                }
                SharedPrefUtils.setIntegerPreference(ctx, ACTIVATION_KEY, activation);
                jobFinished(params, false);
            }

            @Override
            public void onFailure(Call<InfoResponse> call, Throwable t) {
                Log.d(TAG, "FAILED UPDATE: Job finished");
                jobFinished(params, false);
            }
        });
    }*/


    public void  sendInfoEdison(final Context ctx, final String imei1, String imei2, String mac, String android_id, String sim1, String sim2, final String activated, String model, final JobParameters params){
        Log.d(TAG, "sendInfo: active status: "+ activated);
        //SharedPrefUtils.setIntegerPreference(ctx, ACTIVATION_KEY, Integer.valueOf(activated));
        PostInfo postInfo = new PostInfo(imei1,imei2,mac,android_id,sim1,sim2, activated,model);
        long info_id = SharedPrefUtils.getLongPreference(ctx, EDISON_INFO_IMEI, 0);
        if(info_id == 0) {
            String url = "http://202.0.94.14:7890/api/erms/bKashStore";
            tokenDataAPIService.saveInfoEdison(url, imei1, imei2, mac, android_id, sim1, sim2, activated, model).enqueue(new Callback<PostResponseEdisonBokachoda>() {
                @Override
                public void onResponse(Call<PostResponseEdisonBokachoda> call, Response<PostResponseEdisonBokachoda> response) {
                    if (response.body().getCode() == 200) {
                        SharedPrefUtils.setLongPreference(ctx, EDISON_INFO_IMEI, response.body().getStatus());
                        Log.d(TAGE, "SUCCESS POST: Job finished");
                    } else if (response.body().getCode() == 204) {
                        SharedPrefUtils.setLongPreference(ctx, EDISON_INFO_IMEI, response.body().getStatus());
                        Log.d(TAGE, "SUCCESS POST:");
                    }
                    jobFinished(params, false);
                }

                @Override
                public void onFailure(Call<PostResponseEdisonBokachoda> call, Throwable t) {
                    Log.d(TAG, "FAILED POST: Job finished");
                    //getActivationFromServer(ctx, imei1, params);
                    jobFinished(params, false);


                }
            });
            Log.d(TAGE , String.valueOf(info_id));
        } else {
            updateInfoEdison(ctx, postInfo, params);
        }
    }


    public void updateInfoEdison(Context ctx, final PostInfo postInfo, final JobParameters params){
        long info_imei = SharedPrefUtils.getLongPreference(ctx, EDISON_INFO_IMEI, 0);
        String url = "http://202.0.94.14:7890/api/erms/bKashUpdate?uIMEI1="+String.valueOf(info_imei);
        tokenDataAPIService.updateInfoLocal(url, postInfo).enqueue(new Callback<UpdateResponse>() {
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

}
