package com.symphony.bkashg.util;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.symphony.bkashg.R;

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

    public String getModelName(){
        String modelName = getSystemProperty("ro.product.device") ;
        return modelName;
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
