package com.symphony.bkash.onesignal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;
import com.symphony.bkash.R;
import com.symphony.bkash.bKashApp;

import org.json.JSONObject;

import java.math.BigInteger;

public class MyNotificationExtenderService extends NotificationExtenderService {
    String bigPicture;
    String activityToBeOpened;
    String link;
    String modelSWVersion;
    String title,body, t,b, notificationID, notificationType;
    boolean returnVal;
    JSONObject data;
    String customKey;

    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {

        data = notification.payload.additionalData;
        link = notification.payload.launchURL;
        bigPicture = notification.payload.bigPicture;
        modelSWVersion = data.optString("modelSWVersion", null);
        title = notification.payload.title;
        body = notification.payload.body;
        activityToBeOpened = data.optString("activityToBeOpened", null);
        t = data.optString("t", null); // useless
        b = data.optString("b", null); // useless
        notificationID = notification.payload.notificationID;


        notificationType = "promo";

        if(link != null){
            notificationType = "linker";
        }



        OverrideSettings overrideSettings = new OverrideSettings();



        //int minKey = databaseHandler.getMinRowId();
        //rowsToDelete = null;
        // if(databaseHandler.getNotificationCount() > maxStoredNews) {
        //   autoDeleteFromList();
        //}



        if (modelSWVersion != null && (modelSWVersion.equals(getSystemProperty("ro.custom.build.version"))) || (modelSWVersion.equals(getSystemProperty("ro.build.display.id")))) {
//            if(!databaseHandler.CheckIsDataAlreadyInDBorNot(DatabaseHandler.notification_id, notificationID)){
//                insertDB();
//            }
            overrideSettings.extender = new NotificationCompat.Extender() {
                @Override
                public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                    // Sets the background notification color to Red on Android 5.0+ devices.
                    Bitmap icon = BitmapFactory.decodeResource(bKashApp.getContext().getResources(),
                            R.drawable.ic_launcher);
                    builder.setLargeIcon(icon);
                    return builder.setColor(new BigInteger("FF0000FF", 16).intValue());
                }
            };

            OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
            Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);

        }

        else if(modelSWVersion != null && modelSWVersion.equals("any")){
            // long count = databaseHandler.getNotificationCount();
//            if(!databaseHandler.CheckIsDataAlreadyInDBorNot(DatabaseHandler.notification_id, notificationID)){
//                insertDB();
//            }
            //  insertDB();
            overrideSettings.extender = new NotificationCompat.Extender() {
                @Override
                public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                    // Sets the background notification color to Red on Android 5.0+ devices.
                    Bitmap icon = BitmapFactory.decodeResource(bKashApp.getContext().getResources(),
                            R.drawable.ic_launcher);
                    builder.setLargeIcon(icon);
                    return builder.setColor(new BigInteger("FF0000FF", 16).intValue());
                }
            };

            OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
            Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);
        }

        return true;
    }

    public String getSystemProperty(String key) {
        String value = null;

        try {
            value = (String) Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class).invoke(null, key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

}
