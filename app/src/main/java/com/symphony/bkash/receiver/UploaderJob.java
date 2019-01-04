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

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by monir.sobuj on 12/3/2018.
 */

public class UploaderJob extends Job {
    static final String TAG = "BKASH_TAG";
    public String packagename = "com.bKash.customerapp";
    public static final String SIM_Number = "23382346";
    private TokenDataApiService tokenDataAPIService = TokenDataApiUtils.getUserDataAPIServices();
    SharedPreferences.Editor editor;
    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        Context ctx = getContext();
        String mac = "00:00:00:00:00";
        String activated = "0";
        String modelPref = "Symphony ", model;
        PackageManager pm = ctx.getPackageManager();
        model = ConnectionUtils.getSystemProperty("ro.product.device");
        if(model == null || model.isEmpty()){
            model = ConnectionUtils.getSystemProperty("ro.build.product");
        }
        activated = String.valueOf(ConnectionUtils.isPackageInstalled(packagename,pm));
        TelephonyManager telemamanger = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if(ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            String getSimSerialNumber = telemamanger.getSimSerialNumber();
            //String getSimNumber = telemamanger.getLine1Number();
            String imei1 = telemamanger.getImei(0);
            String imei2 = telemamanger.getImei(1);
            mac = ConnectionUtils.getMAC();
            String android_id = Settings.Secure.getString(ctx.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            SharedPreferences prefs = ctx.getSharedPreferences("PREF_BKASH_INSTALLER", MODE_PRIVATE);
            editor = ctx.getSharedPreferences("PREF_BKASH_INSTALLER", MODE_PRIVATE).edit();
            long info_id = prefs.getLong("info_id", 0);
            if(info_id == 0) {
                sendInfo(imei1, imei2, mac, android_id, getSimSerialNumber, activated, modelPref + model);
            } else {
                updateInfo(info_id, imei1, imei2, mac, android_id, getSimSerialNumber, activated, modelPref + model);
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


    public void sendInfo(String imei1,String imei2,String mac, String android_id, final String sim, String activated, String model){
        tokenDataAPIService.saveInfo(imei1, imei2, mac,android_id,sim,activated,model).enqueue(new Callback<PostUserInfo>() {
            @Override
            public void onResponse(Call<PostUserInfo> call, Response<PostUserInfo> response) {
                if(response.body().getCode() == 200) {
                    editor.putLong("info_id", response.body().getStatus());
                    editor.putString(SIM_Number, sim);
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<PostUserInfo> call, Throwable t) {
                Log.e("POST_STATUS", "Unable to submit post to API.");
            }
        });
    }

    public void updateInfo(long id, String imei1,String imei2,String mac, String android_id, String sim, String activated, String model){
        PostInfo postInfo = new PostInfo(imei1,imei2,mac,android_id,sim,activated,model);
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
