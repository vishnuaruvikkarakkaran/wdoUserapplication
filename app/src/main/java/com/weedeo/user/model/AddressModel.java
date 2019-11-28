package com.weedeo.user.model;


public class AddressModel {


    /**
     * name : string
     * pincode : string
     * address_line1 : string
     * address_line2 : string
     * mobile_number : string
     * primary_status : false
     * city : {"name":"hyderabad","state":"maharashtra","status":1,"latitude":17.375278,"longitude":78.474444,"id":"5d96e3d60c737f4ca185a4cb","createdAt":"2019-10-22T04:58:21.816Z"}
     * state : string
     * land_mark : string
     * secondary_number : string
     * user_id : string
     */

    private String name;
    private String pincode;
    private String address_line1;
    private String address_line2;
    private String mobile_number;
    private boolean primary_status;
    private CityBean city;
    private String state;
    private String land_mark;
    private String secondary_number;
    private String user_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress_line1() {
        return address_line1;
    }

    public void setAddress_line1(String address_line1) {
        this.address_line1 = address_line1;
    }

    public String getAddress_line2() {
        return address_line2;
    }

    public void setAddress_line2(String address_line2) {
        this.address_line2 = address_line2;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public boolean isPrimary_status() {
        return primary_status;
    }

    public void setPrimary_status(boolean primary_status) {
        this.primary_status = primary_status;
    }

    public CityBean getCity() {
        return city;
    }

    public void setCity(CityBean city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLand_mark() {
        return land_mark;
    }

    public void setLand_mark(String land_mark) {
        this.land_mark = land_mark;
    }

    public String getSecondary_number() {
        return secondary_number;
    }

    public void setSecondary_number(String secondary_number) {
        this.secondary_number = secondary_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public static class CityBean {
        /**
         * name : hyderabad
         * state : maharashtra
         * status : 1
         * latitude : 17.375278
         * longitude : 78.474444
         * id : 5d96e3d60c737f4ca185a4cb
         * createdAt : 2019-10-22T04:58:21.816Z
         */

        private String name;
        private String state;
        private String status;
        private String latitude;
        private String longitude;
        private String id;
        private String createdAt;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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
    }
}
