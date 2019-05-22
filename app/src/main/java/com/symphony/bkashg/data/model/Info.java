package com.symphony.bkashg.data.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("IMEI1")
    @Expose
    private String iMEI1;
    @SerializedName("IMEI2")
    @Expose
    private String iMEI2;
    @SerializedName("MAC")
    @Expose
    private String mAC;
    @SerializedName("ANDROID_ID")
    @Expose
    private String androidId;
    @SerializedName("sim1")
    @Expose
    private String sim1;
    @SerializedName("sim2")
    @Expose
    private String sim2;
    @NonNull
    @SerializedName("Activated")
    @Expose
    private int activated;
    @SerializedName("Model")
    @Expose
    private String model;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getiMEI1() {
        return iMEI1;
    }

    public void setiMEI1(String iMEI1) {
        this.iMEI1 = iMEI1;
    }

    public String getiMEI2() {
        return iMEI2;
    }

    public void setiMEI2(String iMEI2) {
        this.iMEI2 = iMEI2;
    }

    public String getmAC() {
        return mAC;
    }

    public void setmAC(String mAC) {
        this.mAC = mAC;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getSim1() {
        return sim1;
    }

    public void setSim1(String sim1) {
        this.sim1 = sim1;
    }

    public String getSim2() {
        return sim2;
    }

    public void setSim2(String sim2) {
        this.sim2 = sim2;
    }

    @NonNull
    public int getActivated() {
        return activated;
    }

    public void setActivated(@NonNull int activated) {
        this.activated = activated;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
