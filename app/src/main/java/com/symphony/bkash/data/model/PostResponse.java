package com.symphony.bkash.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostResponse {
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("code")
    @Expose
    private int code;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "PostResponse{" +
                "id=" + id +
                ", code=" + code +
                '}';
    }
}
