package com.symphony.bkash.onesignal;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.symphony.bkash.FirstActivity;
import com.symphony.bkash.NewsActivity;
import com.symphony.bkash.NewsWebActivity;
import com.symphony.bkash.bKashApp;

import org.json.JSONObject;

public class MyNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler{
    // This fires when a notification is opened by tapping on it.
    String bigPicture, bigIcon;
    String customKey;
    String action_url;
    String apkname = "";

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        bigPicture = result.notification.payload.bigPicture;
        bigIcon = result.notification.payload.largeIcon;
        JSONObject data = result.notification.payload.additionalData;
        String link = result.notification.payload.launchURL;
        String activityToBeOpened;
        String packageName;

        //While sending a Push notification from OneSignal dashboard
        // you can send an addtional data named "activityToBeOpened" and retrieve the value of it and do necessary operation
        //If key is "activityToBeOpened" and value is "AnotherActivity", then when a user clicks
        //on the notification, AnotherActivity will be opened.
        //Else, if we have not set any additional data MainActivity is opened.
        if(data!= null){

            customKey = data.optString("customkey", null);
            if (customKey != null) {
                Log.i("OneSignalExample", "customkey set with value: " + customKey);

            }

            activityToBeOpened = data.optString("activityToBeOpened", null);
            action_url = data.optString("action_url", null);
            apkname = data.optString("apkname", null);
            // String title = data.optString("t", null);
            // String body = data.optString("b", null);
            String str1 = result.notification.payload.title;
            String str2 = result.notification.payload.body;
            if(activityToBeOpened != null && activityToBeOpened.equals("NewsActivity")){
                Log.i("OneSignalExample", "customkey set with value: " + activityToBeOpened);
                Intent intent = new Intent(bKashApp.getContext(), NewsActivity.class);
                intent.putExtra("SYSTRAY","systray");
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

                // intent.putExtra("title", title);
                //intent.putExtra("body", body);
                intent.putExtra("title", str1);
                intent.putExtra("body", str2);
                if(bigPicture != null){
                    intent.putExtra("IMAGEURL", bigPicture);
                }
                else{
                    intent.putExtra("IMAGEURL", bigIcon);
                }
                bKashApp.getContext().startActivity(intent);
            }


            else if(activityToBeOpened != null && activityToBeOpened.equals("MainActivity")){
                Log.i("OneSignalExample", "customkey set with value: " + activityToBeOpened);
                Intent intent = new Intent(bKashApp.getContext(), FirstActivity.class);
                intent.putExtra("SYSTRAY","systray");
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                bKashApp.getContext().startActivity(intent);
            }

            else if(activityToBeOpened != null && activityToBeOpened.equals("MediaTekFOTA")){
                Log.i("OneSignalExample", "customkey set with value: " + activityToBeOpened);

                Intent LaunchIntent = bKashApp.getContext().getPackageManager().getLaunchIntentForPackage("com.mediatek.systemupdate");
                LaunchIntent.putExtra("SYSTRAY","systray");
                LaunchIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                bKashApp.getContext().startActivity(LaunchIntent);


                //Intent intent1 = new Intent(Intent.)
                //Uri fotaActivity = Uri.parse("com.mediatek.systemupdate.OtaPkgManagerActivity");
                //// Intent intent1 = new Intent(Intent.ACTION_VIEW, fotaActivity);
                ////Intent intent = new Intent();
                // intent.setComponent(new ComponentName("com.mediatek.systemupdate","com.mediatek.systemupdate.OtaPkgManagerActivity"));
                ////  intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                //MySymphonyApp.getContext().startActivity(intent1);
            }

            else if(activityToBeOpened != null && activityToBeOpened.equals("SpedturmFOTA")){
                Log.i("OneSignalExample", "customkey set with value: " + activityToBeOpened);
                Intent LaunchIntent = bKashApp.getContext().getPackageManager().getLaunchIntentForPackage("com.megafone.systemupdate");
                LaunchIntent.putExtra("SYSTRAY","systray");
                LaunchIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                bKashApp.getContext().startActivity(LaunchIntent);
            }

            else if(activityToBeOpened != null && activityToBeOpened.equals("UniversalFOTA")){
                Log.i("OneSignalExample", "customkey set with value: " + activityToBeOpened);
                Intent LaunchIntent = bKashApp.getContext().getPackageManager().getLaunchIntentForPackage("com.google.android.gms");
                LaunchIntent.putExtra("SYSTRAY","systray");
                LaunchIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                bKashApp.getContext().startActivity(LaunchIntent);
            }

            else if(link!=null){
                Intent intent = new Intent(bKashApp.getContext(), NewsWebActivity.class);
                Log.d("URLPush", link);
                packageName = data.optString("packageName", null);

                if(link.contains("https://play.google.com/store/")){
                    Log.d("URLPushDetect", "Detected");
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id="+packageName));
                    bKashApp.getContext().startActivity(intent);
                }
                else{
                    Log.d("URLPushDetect", "Normal");
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("targetUrl", link);
                    intent.putExtra("SYSTRAY","systray");


                    bKashApp.getContext().startActivity(intent);
                }

            }

            else if (action_url.length() >4 && actionType == OSNotificationAction.ActionType.ActionTaken) {

                Intent intent = new Intent(bKashApp.getContext(), FirstActivity.class);

                intent.putExtra("action_url", action_url);
                intent.putExtra("apkname", apkname);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
                bKashApp.getContext().startActivity(intent);
            }

            //else if(result.notification.payload.)

            else{
                Intent intent = new Intent(bKashApp.getContext(), FirstActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                bKashApp.getContext().startActivity(intent);
            }

        }





        //If we send notification with action buttons we need to specidy the button id's and retrieve it to
        //do the necessary operation.
        if(actionType == OSNotificationAction.ActionType.ActionTaken){
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
            if (result.action.actionID.equals("ActionOne")) {
                Toast.makeText(bKashApp.getContext(), "ActionOne Button was pressed", Toast.LENGTH_LONG).show();
            } else if (result.action.actionID.equals("ActionTwo")) {
                Toast.makeText(bKashApp.getContext(), "ActionTwo Button was pressed", Toast.LENGTH_LONG).show();
            }
        }



    }
}
