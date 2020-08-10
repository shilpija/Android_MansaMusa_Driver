package com.mansamusa.driver.model;

import com.google.gson.annotations.SerializedName;

public class LoginComResponse {
    @SerializedName("access_token")
    private String access_token;
    @SerializedName("msg")
    private String msg;
    @SerializedName("code")
    private int code;
    @SerializedName("data")
    private LoginComResponse data;
    @SerializedName("error")
    private LoginComResponse error;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LoginComResponse getData() {
        return data;
    }

    public void setData(LoginComResponse data) {
        this.data = data;
    }

    public LoginComResponse getError() {
        return error;
    }

    public void setError(LoginComResponse error) {
        this.error = error;
    }
}
