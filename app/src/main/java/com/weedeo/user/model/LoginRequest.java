package com.weedeo.user.model;

/**
 * Created By Athul on 10-10-2019.
 */
public class LoginRequest {


    /**
     * mobile : 9995212345
     * device_id : abc2
     * user_type : shop_admin
     * fcm_uid : 90joljlomjgiuh
     */

    private String mobile;
    private String device_id;
    private String user_type;
    private String fcm_uid;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getFcm_uid() {
        return fcm_uid;
    }

    public void setFcm_uid(String fcm_uid) {
        this.fcm_uid = fcm_uid;
    }
}
