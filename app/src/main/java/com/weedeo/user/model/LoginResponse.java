package com.weedeo.user.model;

public class LoginResponse {

    /**
     * data : {"mobile":"+917012181745","role":4,"online_status":false,"block_status":false,"profile_pic":"default.jpg","fcm_uid":"l6ktW9DZAXX6y8iTffDriZn6zKn1","id":"5da41692605227000724b902","createdAt":"2019-10-14T06:32:50.960Z","updatedAt":"2019-10-14T06:32:50.960Z"}
     * message : Get user details successfully
     * status : success
     * statusCode : 200
     * token : xMjZWJ2MyggqaFCrZRej3usJ378RbF62CGelDVdOtr2QIvyTbwOzKmBaPIzpEJH2
     */

    private DataBean data;
    private String message;
    private String status;
    private int statusCode;
    private String token;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class DataBean {
        /**
         * name : athul
         * email : athul@yopmail.com
         * mobile : +917012181745
         * role : 4
         * online_status : false
         * block_status : false
         * profile_pic : default.jpg
         * fcm_uid : l6ktW9DZAXX6y8iTffDriZn6zKn1
         * id : 5da41692605227000724b902
         * createdAt : 2019-10-14T06:32:50.960Z
         * updatedAt : 2019-10-14T06:32:50.960Z
         */

        private String name;
        private String mobile;
        private String email;
        private int role;
        private boolean online_status;
        private boolean block_status;
        private String profile_pic;
        private String fcm_uid;
        private String id;
        private String createdAt;
        private String updatedAt;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public boolean isOnline_status() {
            return online_status;
        }

        public void setOnline_status(boolean online_status) {
            this.online_status = online_status;
        }

        public boolean isBlock_status() {
            return block_status;
        }

        public void setBlock_status(boolean block_status) {
            this.block_status = block_status;
        }

        public String getProfile_pic() {
            return profile_pic;
        }

        public void setProfile_pic(String profile_pic) {
            this.profile_pic = profile_pic;
        }

        public String getFcm_uid() {
            return fcm_uid;
        }

        public void setFcm_uid(String fcm_uid) {
            this.fcm_uid = fcm_uid;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
