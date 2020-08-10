package com.mansamusa.driver.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CommonResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("dish_id")
    private String dish_id;
 @SerializedName("fCamera")
    private String fCamera;

    @SerializedName("image")
    private String image;

    @SerializedName("msg")
    private String msg;
    @SerializedName("order_id")
    private String order_id;
    @SerializedName("product_id")
    private String product_id;

    @SerializedName("message")
    private String message;

    @SerializedName("token")
    private String token;

    @SerializedName("order_status")
    private String order_status;

    @SerializedName("username")
    private String username;

    @SerializedName("phone_number")
    private String phone_number;

    @SerializedName("city")
    private String city;

    @SerializedName("name")
    private String name;

    @SerializedName("dish_image")
    private String dish_image;

    @SerializedName("price")
    private String price;

    @SerializedName("total")
    private String total;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("parcel_image")
    private String parcel_image;

    @SerializedName("location")
    private String location;

    @SerializedName("payment_method")
    private String payment_method;

    @SerializedName("building_no")
    private String building_no;

    @SerializedName("flat_no")
    private String flat_no;

    @SerializedName("floor_no")
    private String floor_no;

    @SerializedName("landmark")
    private String landmark;

    @SerializedName("success")
    private CommonResponse success;

    @SerializedName("error")
    private CommonResponse error;

    @SerializedName("kitchen_location")
    private CommonResponse kitchen_location;

    @SerializedName("user_info")
    private CommonResponse user_info;

    @SerializedName("delivery_address")
    private CommonResponse delivery_address;

    @SerializedName("data")
    private ArrayList<CommonResponse> data;

    @SerializedName("parceldata")
    private CommonResponse objdata;

    @SerializedName("orderitems")
    private ArrayList<CommonResponse> orderitems;

    public CommonResponse getObjdata() {
        return objdata;
    }

    public void setObjdata(CommonResponse objdata) {
        this.objdata = objdata;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CommonResponse getSuccess() {
        return success;
    }

    public void setSuccess(CommonResponse success) {
        this.success = success;
    }

    public CommonResponse getError() {
        return error;
    }

    public void setError(CommonResponse error) {
        this.error = error;
    }


    public ArrayList<CommonResponse> getData() {
        return data;
    }

    public void setData(ArrayList<CommonResponse> data) {
        this.data = data;
    }

    public CommonResponse getKitchen_location() {
        return kitchen_location;
    }

    public void setKitchen_location(CommonResponse kitchen_location) {
        this.kitchen_location = kitchen_location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ArrayList<CommonResponse> getOrderitems() {
        return orderitems;
    }

    public void setOrderitems(ArrayList<CommonResponse> orderitems) {
        this.orderitems = orderitems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDish_image() {
        return dish_image;
    }

    public void setDish_image(String dish_image) {
        this.dish_image = dish_image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public CommonResponse getUser_info() {
        return user_info;
    }

    public void setUser_info(CommonResponse user_info) {
        this.user_info = user_info;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public CommonResponse getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(CommonResponse delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getParcel_image() {
        return parcel_image;
    }

    public void setParcel_image(String parcel_image) {
        this.parcel_image = parcel_image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getBuilding_no() {
        return building_no;
    }

    public void setBuilding_no(String building_no) {
        this.building_no = building_no;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;
    }

    public String getFloor_no() {
        return floor_no;
    }

    public void setFloor_no(String floor_no) {
        this.floor_no = floor_no;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getfCamera() {
        return fCamera;
    }

    public void setfCamera(String fCamera) {
        this.fCamera = fCamera;
    }

    public String getDish_id() {
        return dish_id;
    }

    public void setDish_id(String dish_id) {
        this.dish_id = dish_id;
    }
}
