package com.symphony.bkashg;

import android.app.Application;
import android.content.Context;

import com.facebook.ads.AudienceNetworkAds;
import com.onesignal.OneSignal;
import com.symphony.bkashg.onesignal.MyNotificationOpenedHandler;
import com.symphony.bkashg.onesignal.MyNotificationReceivedHandler;

public class bKashApp extends Application {
    private static bKashApp mInstance;
    private static Context context;
    public static Context getContext() {
        return context;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        context = getApplicationContext();

        // OneSignal Initialization
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                .setNotificationReceivedHandler( new MyNotificationReceivedHandler() )
                .init();

        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);
    }

    public static synchronized bKashApp getmInstance(){
        return mInstance;
    }
}
