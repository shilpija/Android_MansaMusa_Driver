package com.mansamusa.driver.model;

import com.google.gson.annotations.SerializedName;

public class DriverOrderDetails {
    @SerializedName("msg")
    private String msg;
    @SerializedName("code")
    private int code;
    @SerializedName("error")
    private DriverOrderDetails error;
    @SerializedName("success")
    private DriverOrderDetails success;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DriverOrderDetails getSuccess() {
        return success;
    }

    public void setSuccess(DriverOrderDetails success) {
        this.success = success;
    }

    public DriverOrderDetails getError() {
        return error;
    }

    public void setError(DriverOrderDetails error) {
        this.error = error;
    }
}
