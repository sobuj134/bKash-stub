package com.symphony.bkash.util;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.symphony.bkash.R;

public class RemoteConfig {
    public String modelName;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    public RemoteConfig (){

    }

    public FirebaseRemoteConfig getmFirebaseRemoteConfig(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(remoteConfigSettings);

        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_default);

        return  mFirebaseRemoteConfig;
    }
}
