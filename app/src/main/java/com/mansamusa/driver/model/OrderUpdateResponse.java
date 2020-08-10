package com.mansamusa.driver.model;

public class OrderUpdateResponse {

    /**
     * success : Your order has been updated successfully.
     * code : 200
     */

    private String success;
    private int code;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
