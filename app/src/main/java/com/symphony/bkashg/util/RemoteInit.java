package com.symphony.bkashg.util;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.symphony.bkashg.R;

public class RemoteInit  {
    public  String PACKAGE_NAME_LAUNCHING_APP = "com.bKash.customerapp";
    public String STORE_LINK = "https://play.google.com/store/";

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    public RemoteInit(FirebaseRemoteConfig mFirebaseRemoteConfig) {
        this.mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(remoteConfigSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_default);
    }



//    public void fetchRemoteValue(){
//        PACKAGE_NAME_LAUNCHING_APP = mFirebaseRemoteConfig.getString(PACKAGE_NAME_LAUNCHING_APP);
//        STORE_LINK = mFirebaseRemoteConfig.getString(STORE_LINK);
//
//        long cacheExpiration = 3600; // 1 hour in seconds.
//        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
//        // retrieve values from the service.
//        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
//            cacheExpiration = 0;
//        }
//
//        // [START fetch_config_with_callback]
//        // cacheExpirationSeconds is set to cacheExpiration here, indicating the next fetch request
//        // will use fetch data from the Remote Config service, rather than cached parameter values,
//        // if cached parameter values are more than cacheExpiration seconds old.
//        // See Best Practices in the README for more information.
//        mFirebaseRemoteConfig.fetch(cacheExpiration)
//
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d( "FETCH_STATUS",
//                                    "Fetch Successful");
//
//                            // After config data is successfully fetched, it must be activated before newly fetched
//                            // values are returned.
//                            mFirebaseRemoteConfig.activateFetched();
//                        } else {
//                            Log.d( "FETCH_STATUS",
//                                    "Fetch Falied");
//                        }
//                        //displayWelcomeMessage();
//                    }
//                });
//// [END fetch_config_with_callback]
//    }
}
