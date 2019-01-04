package com.symphony.bkash.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostUserInfo {
    @SerializedName("status")
    @Expose
    private long status;
    @SerializedName("code")
    @Expose
    private int code;

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "PostUserInfo{" +
                "status=" + status +
                ", code=" + code +
                '}';
    }
}
