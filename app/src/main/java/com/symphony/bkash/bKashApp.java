package com.symphony.bkash;

import android.app.Application;
import android.content.Context;

import com.onesignal.OneSignal;
import com.symphony.bkash.onesignal.MyNotificationOpenedHandler;
import com.symphony.bkash.onesignal.MyNotificationReceivedHandler;

public class bKashApp extends Application {
    private static bKashApp mInstance;
    private static Context context;
    public static Context getContext() {
        return context;
    }
        @Override
        public void onCreate() {
            super.onCreate();

            // OneSignal Initialization
            OneSignal.startInit(this)
                    .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                    .setNotificationReceivedHandler( new MyNotificationReceivedHandler() )
                    .init();
        }

    public static synchronized bKashApp getmInstance(){
        return mInstance;
    }
}
