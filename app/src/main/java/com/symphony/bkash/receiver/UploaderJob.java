package com.symphony.bkash.receiver;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.symphony.bkash.data.model.BkashResponse;
import com.symphony.bkash.data.model.PostInfo;
import com.symphony.bkash.data.model.PostUserInfo;
import com.symphony.bkash.data.remote.TokenDataApiService;
import com.symphony.bkash.data.remote.TokenDataApiUtils;
import com.symphony.bkash.util.ConnectionUtils;
import com.symphony.bkash.util.SharedPrefUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by monir.sobuj on 12/3/2018.
 */

public class UploaderJob extends Job {
    public static final String INFO_ID_KEY = "bkash_sym_info_id";
    public static final String ACTIVATION_KEY = "bkash_sym_active_status";
    static final String TAG = "BKASH_TAG";
    public String packagename = "com.bKash.customerapp";
    public static final String SIM_Number = "23382346";
    private TokenDataApiService tokenDataAPIService = TokenDataApiUtils.getUserDataAPIServices();
    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        Context ctx = getContext();
        //IMEI1, IMEI2, MAC, AndroidID, SIM1, SIM2, Model, Activated

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

        // activation algorithm
        //0 - Not Acivated
        //1 - Activated
        //2 - Independent Install
        //3 - App Removed
        boolean isActivated = ConnectionUtils.isPackageInstalled(packagename,ctx.getPackageManager());
        int prevActivated = SharedPrefUtils.getIntegerPreference(ctx, ACTIVATION_KEY, 0);
        int activated = 0;
        if(isActivated){
            switch (prevActivated){
                case 0:
                    activated = 2;
                    break;
                case 1:
                    activated = prevActivated;
                    break;
                case 2:
                    activated = prevActivated;
                    break;
                case 3:
                    activated = 2;
                    break;
                    default:
                        break;
            }
        } else{
            switch (prevActivated){
                case 0:
                    activated = prevActivated;
                    break;
                case 1:
                    activated = 3;
                    break;
                case 2:
                    activated = 3;
                    break;
                case 3:
                    activated = prevActivated;
                    break;
                default:
                    break;
            }
        }
        SharedPrefUtils.setIntegerPreference(ctx, ACTIVATION_KEY, activated);
        String mac = "00:00:00:00:00";
        String modelPref = "Symphony ", model;
        model = ConnectionUtils.getSystemProperty("ro.product.device");
        if(model == null || model.isEmpty()){
            model = ConnectionUtils.getSystemProperty("ro.build.product");
        }
        TelephonyManager telemamanger = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if(ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

            String imei1 = telemamanger.getImei(0);
            String imei2 = telemamanger.getImei(1);
            mac = ConnectionUtils.getMAC();
            String android_id = Settings.Secure.getString(ctx.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            long info_id = SharedPrefUtils.getLongPreference(ctx, INFO_ID_KEY, 0);
            if(info_id == 0) {
                sendInfo(ctx, imei1, imei2, mac, android_id, sim1, sim2, String.valueOf(activated), modelPref + model);
            } else {
                updateInfo(info_id, imei1, imei2, mac, android_id, sim1, sim2, String.valueOf(activated), modelPref + model);
            }
        }

        return Result.SUCCESS;
    }


    public static void schedulePeriodic() {
        new JobRequest.Builder(UploaderJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                .setUpdateCurrent(true)
                .setPersisted(true)
                .build()
                .schedule();
    }


    public void sendInfo(final Context ctx, String imei1,String imei2,String mac, String android_id, String sim1, String sim2, String activated, String model){
        tokenDataAPIService.saveInfo(imei1, imei2, mac, android_id, sim1, sim2, activated, model).enqueue(new Callback<PostUserInfo>() {
            @Override
            public void onResponse(Call<PostUserInfo> call, Response<PostUserInfo> response) {
                if(response.body().getCode() == 200) {
                    SharedPrefUtils.setLongPreference(ctx, INFO_ID_KEY, response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<PostUserInfo> call, Throwable t) {
                Log.e("POST_STATUS", "Unable to submit post to API.");
            }
        });
    }

    public void updateInfo(long id, String imei1,String imei2,String mac, String android_id, String sim1, String sim2, String activated, String model){
        PostInfo postInfo = new PostInfo(imei1,imei2,mac,android_id,sim1,sim2, activated,model);
        tokenDataAPIService.updateInfo(id, postInfo).enqueue(new Callback<BkashResponse>() {
            @Override
            public void onResponse(Call<BkashResponse> call, Response<BkashResponse> response) {

                if(response.body().getCode().equals("200")){
                    Log.i("UPDATE_STATUS", "updated to API." + response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<BkashResponse> call, Throwable t) {
                Log.e("UPDATE_STATUS", "Unable to update to API.");
            }
        });
    }






}
