package com.mansamusa.driver.retrofit;


import com.mansamusa.driver.model.CommonResponse;
import com.mansamusa.driver.model.CompaniesListResponse;
import com.mansamusa.driver.model.DriverOrderDetails;
import com.mansamusa.driver.model.LoginComResponse;
import com.mansamusa.driver.model.OrderUpdateResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by mahipal on 26/Sep/18.
 */

public interface ServicesInterface {

    @FormUrlEncoded
    @POST("api/driverlogin")
    Call<CommonResponse> loginDetails(@Field("username") String username,
                                      @Field("password") String password,
                                      @Field("role") String role,
                                      @Field("device_token") String device_token);


    @GET("api/driverorder")
    Call<CommonResponse> orderDetails(@Query("order_id") String order_id,
                                      @Header("Authorization") String Authorization);


    @FormUrlEncoded
    @POST("api/driveraccept")
    Call<CommonResponse> acceptDetails(@Header("Authorization") String Authorization,
                                       @Field("order_id") String order_id);

    @GET("api/driverorderdetails")
    Call<CommonResponse> driverOrderDetails(@Header("Authorization") String Authorization,
                                            @Query("order_id") String order_id);

    //seller
    @Multipart
    @POST("api/driverorderupdate")
    Call<OrderUpdateResponse> driverOrderUpdateDetails(@Header("Authorization") String Authorization,
                                                       @PartMap Map<String, RequestBody> part);

//  @Part List<MultipartBody.Part> files

    //buyer
    @Multipart
    @POST("api/driverordercomplete")
    Call<OrderUpdateResponse> buyerUpdateDetails(@Header("Authorization") String Authorization,
                                                 @PartMap Map<String, RequestBody> part,
                                                 @Part MultipartBody.Part file);

    //companies list
    @GET("api/companydriver")
    Call<CompaniesListResponse> companiesListDetails();

    @FormUrlEncoded
    @POST("api/companydriverlogin")
    Call<LoginComResponse> loginComDetails(@Field("company_id") String company_id,
                                           @Field("phone_number") String phone_number);

    @Multipart
    @POST("api/driverparcel")
    Call<CommonResponse> driverparcel(@Header("Authorization") String Authorization,
                                      @PartMap Map<String, RequestBody> part,
                                      @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("api/driverorderdeliver")
    Call<DriverOrderDetails> driverorderdeliverDetails(@Header("Authorization") String Authorization,
                                                       @Field("order_id") String order_id);

}







