package com.symphony.bkash.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostInfo {

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
    @SerializedName("SIM1")
    @Expose
    private String sim1;
    @SerializedName("SIM2")
    @Expose
    private String sim2;
    @SerializedName("Activated")
    @Expose
    private String activated;
    @SerializedName("Model")
    @Expose
    private String model;

    /**
     * No args constructor for use in serialization
     *
     */
    public PostInfo() {
    }

    /**
     *
     * @param model
     * @param mAC
     * @param iMEI2
     * @param iMEI1
     * @param activated
     * @param sim1
     * @param sim2
     */
    public PostInfo(String iMEI1, String iMEI2, String mAC, String androidId, String sim1, String sim2, String activated, String model) {
        super();
        this.iMEI1 = iMEI1;
        this.iMEI2 = iMEI2;
        this.mAC = mAC;
        this.sim1 = sim1;
        this.sim2 = sim2;
        this.activated = activated;
        this.model = model;
        this.androidId = androidId;
    }

    public String getIMEI1() {
        return iMEI1;
    }

    public void setIMEI1(String iMEI1) {
        this.iMEI1 = iMEI1;
    }

    public String getIMEI2() {
        return iMEI2;
    }

    public void setIMEI2(String iMEI2) {
        this.iMEI2 = iMEI2;
    }

    public String getMAC() {
        return mAC;
    }

    public void setMAC(String mAC) {
        this.mAC = mAC;
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

    public String getActivated() {
        return activated;
    }

    public void setActivated(String activated) {
        this.activated = activated;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
