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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import com.symphony.bkash.listener.AppUpdateListener;
import com.symphony.bkash.receiver.VersionChecker;
import com.symphony.bkash.util.ConnectionUtils;
import com.symphony.bkash.util.Constant;


public class FirstActivity extends BaseActivity implements AppUpdateListener {

    public static String[] permisionList = { "android.permission.READ_PHONE_STATE","android.permission.READ_CONTACTS"}; //,"android.permission.READ_CONTACTS"
    public static final int permsRequestCode = 20;
    public static final String TAG = "FirstActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        FirstActivity.super.requestAppPermissions(permisionList, R.string.runtime_permissions_txt, permsRequestCode);

    }

    @Override
    protected void onResume() {
        super.onResume();
       if(ConnectionUtils.isNetworkConnected(this)){
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                PackageManager pm = getApplicationContext().getPackageManager();
                if(ConnectionUtils.isPackageInstalled(Constant.PACKAGE_NAME_LAUNCHING_APP, pm)){
                    new VersionChecker(this, Constant.PACKAGE_NAME_LAUNCHING_APP, this);
                    Intent i = getPackageManager().getLaunchIntentForPackage(Constant.PACKAGE_NAME_LAUNCHING_APP);
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
        if (Constant.STORE_LINK.contains("https://play.google.com/store/")) {
            Log.d("URLPushDetect", "Not installed");
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + Constant.PACKAGE_NAME_LAUNCHING_APP));
            startActivity(intent);
        } else{
            Log.d("URLPushDetect", "Normal");
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("targetUrl", Constant.STORE_LINK);
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
}


