package com.mansamusa.driver.retrofitExtra;

/**
 * Created by Mahipal on 2/11/18.
 */
public enum ParamEnum {


    LOGIN("login"),
    PAST("past"),
    UPCOMING("upcoming"),
    PENDING("pending"),
    BOOK("book"),
    SCHEDULE("scheduling"),
    CALL("Call"),
    HOME("home"),
    CURRENT("current"),
    ADDRESS("address"),
    SUCCESS("SUCCESS"),
    ANDROID("android"),
    NORMAL("normal"),
    GOOGLE("google"),
    FACEBOOK("facebook"),
    MALE("m"),
    FEMALE("f"),
    MEMBER_NAME("memberName"),
    RELATIONSHIP("relation"),
    ID("id"),
    NUMBER("number"),
    DR_DETAILS("dr_details"),
    CHECK_BOOKING("check_booking"),
    PROMO_CODE("promo_code"),
    NAME("name"),
    IMAGE("image"),
    DIGITAL_SIGN("digital_sign"),
    MEMBER("member"),
    USER("user"),
    CHAT("adminChatUser"),
    WEB_URL("URL"),
    CHAT_HEAD("chat_data"),
    TYPE("type"),
    OTP("otp"),
    MSG("message"),
    BADGE_COUNT("badge"),
    TITLE("title"),
    TOKEN_EXPIRE("tokenexpire"),
    STATUS("status"),
    HINDI("hi"),
    ENGLISH("en"),
    FROM("from");

    private final String value;

    ParamEnum(String value) {
        this.value=value;
    }

    ParamEnum(){
     this.value=null;
    }

    public String theValue() {
        return this.value;
    }

}