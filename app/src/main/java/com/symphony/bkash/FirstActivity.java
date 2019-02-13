package com.symphony.bkash;

import android.Manifest;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.symphony.bkash.listener.AppUpdateListener;
import com.symphony.bkash.receiver.VersionChecker;
import com.symphony.bkash.util.ConnectionUtils;
import com.symphony.bkash.util.Constant;
import com.symphony.bkash.util.RemoteConfig;

import java.util.Arrays;
import java.util.List;

import static com.symphony.bkash.util.Constant.PACKAGE_NAME_LAUNCHING_APP;
import static com.symphony.bkash.util.Constant.STORE_LINK;


public class FirstActivity extends BaseActivity implements AppUpdateListener {

    public static String[] permisionList = { "android.permission.READ_PHONE_STATE","android.permission.READ_CONTACTS"}; //,"android.permission.READ_CONTACTS"
    public static final int permsRequestCode = 20;
    public static final String TAG = "FirstActivity";
    public FirebaseRemoteConfig mFirebaseRemoteConfig;
    public RemoteConfig remoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_first);
        FirstActivity.super.requestAppPermissions(permisionList, R.string.runtime_permissions_txt, permsRequestCode);

//        fetchRemoteValue();\
        remoteConfig = new RemoteConfig();
        
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseRemoteConfig = remoteConfig.getmFirebaseRemoteConfig();
    }

    @Override
    protected void onResume() {
        super.onResume();
       if(ConnectionUtils.isNetworkConnected(this)){
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                PackageManager pm = getApplicationContext().getPackageManager();
                if(ConnectionUtils.isPackageInstalled(PACKAGE_NAME_LAUNCHING_APP, pm)){
                    new VersionChecker(this, PACKAGE_NAME_LAUNCHING_APP, this);
                    Intent i = getPackageManager().getLaunchIntentForPackage(PACKAGE_NAME_LAUNCHING_APP);
                    startActivity(i);
                } else {
                    gotoPlay();
                }
            } else {
                //finish();
            }
        } else {
            displayNoInternetDialog(this);
        }
        fetchRemoteConfig();
    }



    public void displayNoInternetDialog(final Activity activity){
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setCancelable(false);
        alert.setTitle(getString(R.string.no_connection_title));
        alert.setMessage(getString(R.string.no_connection_msg));
        alert.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                activity.finish();

            }
        });
        alert.show();
    }

    public void gotoPlay(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (STORE_LINK.contains("https://play.google.com/store/")) {
            Log.d("URLPushDetect", "Not installed");
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + PACKAGE_NAME_LAUNCHING_APP));
            startActivity(intent);
        } else{
            Log.d("URLPushDetect", "Normal");
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("targetUrl", STORE_LINK);
            intent.putExtra("SYSTRAY", "systray");
            startActivity(intent);

        }
    }

    @Override
    public void onUpdate() {
        gotoPlay();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case permsRequestCode: {

                if ((grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    scheduleJob();
                } else {
                    finish();
                }
                return;
            }
        }
    }

    private void scheduleJob(){

        ComponentName componentName = new ComponentName(getApplicationContext(), UploaderJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(15  * 60 * 1000)
                .build();
        JobScheduler scheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if(resultCode == JobScheduler.RESULT_SUCCESS){
            Log.d(TAG, "job scheduled");
        } else {
            Log.d(TAG, "job scheduling failed");
        }

    }

    private void cancelJob(){
        JobScheduler scheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG, "cancelJob: Job cancelled");
    }



    public void fetchRemoteConfig() {
        long cacheExpiration = 3600;
        if(mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()){
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    mFirebaseRemoteConfig.activateFetched();
                    Log.d("REMOTE_CONFIG", "Successfull");

                } else {
                    Log.d("REMOTE_CONFIG", "Failed");
                }

                //loadOfferBanner();
                eligibleHandset(FirstActivity.this);
                loadURL();

            }
        });
    }

    public void loadURL(){
        Constant.PACKAGE_NAME_LAUNCHING_APP = mFirebaseRemoteConfig.getString("PACKAGE_NAME_LAUNCHING_APP");
        Constant.STORE_LINK = mFirebaseRemoteConfig.getString("STORE_LINK");
        Log.d("CONSTANT_VAL " , Constant.PACKAGE_NAME_LAUNCHING_APP);
    }

    public void eligibleHandset(final Activity activity) {
        boolean modelExists = false;
        boolean isRestrictionOn = mFirebaseRemoteConfig.getBoolean("RESTRICTION");
        String allowedDevices = mFirebaseRemoteConfig.getString("ALLOWED_DEVICES");
        List<String> restricted_device_list = Arrays.asList(allowedDevices.split("\\s*,\\s*"));
        if(isRestrictionOn){
            modelExists = restricted_device_list.contains(remoteConfig.getModelName());
            if(modelExists){
                return ;
            }
            else{
                final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setCancelable(false);
                alert.setTitle(getString(R.string.not_eligible));
                alert.setMessage(getString(R.string.not_eligible_msg));
                alert.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        activity.finish();

                    }
                });
                alert.show();
                finish();
            }
        }

        else{
            return;
        }
    }

}


