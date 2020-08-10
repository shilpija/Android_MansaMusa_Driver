package com.mansamusa.driver.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CompaniesListResponse {
    @SerializedName("company_id")
    private int company_id;

    @SerializedName("company_name")
    private String company_name;

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private ArrayList<CompaniesListResponse>data;

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public ArrayList<CompaniesListResponse> getData() {
        return data;
    }

    public void setData(ArrayList<CompaniesListResponse> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
