package com.symphony.bkashg;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.symphony.bkashg.listener.AppUpdateListener;
import com.symphony.bkashg.receiver.VersionChecker;
import com.symphony.bkashg.util.ConnectionUtils;
import com.symphony.bkashg.util.Constant;
import com.symphony.bkashg.util.RemoteConfig;

import java.util.Arrays;
import java.util.List;

import static com.symphony.bkashg.util.Constant.PACKAGE_NAME_LAUNCHING_APP;
import static com.symphony.bkashg.util.Constant.STORE_LINK;


public class FirstActivity extends BaseActivity implements AppUpdateListener {

    public static String[] permisionList = { "android.permission.READ_PHONE_STATE","android.permission.READ_CONTACTS"}; //,"android.permission.READ_CONTACTS"
    public static final int permsRequestCode = 20;
    public static final String TAG = "FirstActivity";
    public FirebaseRemoteConfig mFirebaseRemoteConfig;
    public RemoteConfig remoteConfig;
    public boolean approved = true;
    public String auth_msg_title = "Your handset is not eligible!!!";
    public String auth_msg_desc = "This handset is unauthorised. Please use any Authorised Handset";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_first);
        FirstActivity.super.requestAppPermissions(permisionList, R.string.runtime_permissions_txt, permsRequestCode);

//        fetchRemoteValue();\
        remoteConfig = new RemoteConfig();
        mFirebaseRemoteConfig = remoteConfig.getmFirebaseRemoteConfig();
        fetchRemoteConfig();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseRemoteConfig = remoteConfig.getmFirebaseRemoteConfig();

    }

    @Override
    protected void onResume() {
        super.onResume();
        remoteConfig = new RemoteConfig();
        mFirebaseRemoteConfig = remoteConfig.getmFirebaseRemoteConfig();
        fetchRemoteConfig();
        if(ConnectionUtils.isNetworkConnected(this)){
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                PackageManager pm = getApplicationContext().getPackageManager();
                if(approved) {
                    if (ConnectionUtils.isPackageInstalled(PACKAGE_NAME_LAUNCHING_APP, pm)) {
                        new VersionChecker(this, PACKAGE_NAME_LAUNCHING_APP, this);
                        Intent i = getPackageManager().getLaunchIntentForPackage(PACKAGE_NAME_LAUNCHING_APP);
                        startActivity(i);
                    } else {
                        gotoPlay();


                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), getText(R.string.not_eligible),Toast.LENGTH_LONG).show();
                    displayUnApproved(this);
                }

            } else {
                //finish();
            }
        } else {
            displayNoInternetDialog(this);
        }



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

                if (approved && (grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
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
        auth_msg_title = mFirebaseRemoteConfig.getString("not_eligible");
        auth_msg_desc = mFirebaseRemoteConfig.getString("not_eligible_msg");
        List<String> restricted_device_list = Arrays.asList(allowedDevices.split("\\s*,\\s*"));
        if(isRestrictionOn){
            modelExists = restricted_device_list.contains(remoteConfig.getModelName());
            if(modelExists){
                approved = true;
                return ;
            }
            else{
                approved = false;
                sendNotification();
//                final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
//                alert.setCancelable(false);
//                alert.setTitle(getString(R.string.not_eligible));
//                alert.setMessage(getString(R.string.not_eligible_msg));
//                alert.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.dismiss();
//                        activity.finish();
//
//                    }
//                });
//                alert.show();
//                //activity.finish();
//                Toast.makeText(getApplicationContext(), getText(R.string.not_eligible),Toast.LENGTH_LONG).show();
            }
        }

        else{
            return;
        }
    }

    public void sendNotification() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker("Hearty365")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(auth_msg_title)
                .setContentText(auth_msg_desc)
                .setContentInfo("Pirated Handset");
        Log.d("NOTILOG", "my_channel_id_01");

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
    }

    public void displayUnApproved(final Activity activity){
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setCancelable(false);
        alert.setTitle(auth_msg_title);
        alert.setMessage(auth_msg_desc);
        alert.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                activity.finish();

            }
        });
        alert.show();
    }



}


