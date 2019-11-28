package com.weedeo.user.model;

public class ProfileUpdateModel {


    /**
     * name : string
     * gender : string
     * dob : string
     * profile_pic : string
     * user_id : string
     * user_type : string
     * email : string
     * mobile : string
     * fcm_uid : string
     */

    private String name;
    private String gender;
    private String dob;
    private String profile_pic;
    private String user_id;
    private String user_type;
    private String email;
    private String mobile;
    private String fcm_uid;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFcm_uid() {
        return fcm_uid;
    }

    public void setFcm_uid(String fcm_uid) {
        this.fcm_uid = fcm_uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
