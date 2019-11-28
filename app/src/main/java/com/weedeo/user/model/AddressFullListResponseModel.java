package com.weedeo.user.model;

import java.io.Serializable;
import java.util.List;

public class AddressFullListResponseModel  {


    /**
     * data : [{"name":"cjcjcjcjcjf","pincode":868686,"address_line1":"fjfjfjfjcjfjckc","address_line2":"fjfjdjfjfjfjfj","mobile_number":6890906868,"city":{"createdAt":"2019-10-22T07:00:36.344Z","id":"5d96e3d60c737f4ca185a4cb","latitude":17.375278,"longitude":78.474444,"name":"hyderabad","state":"maharashtra","status":1},"state":"Maharashtra","land_mark":"cjcjcjcjcd","secondary_number":9898986868,"user_id":"5da41692605227000724b902","primary_status":false,"status":false,"id":"5daea9232989630007b9c768","createdAt":"2019-10-22T07:00:51.438Z","updatedAt":"2019-10-22T07:00:51.438Z"},{"name":"babhshshshsh","pincode":879799,"address_line1":"agaggsgzgzvzvzv","address_line2":"hahhshshshshshsv","mobile_number":9499797979,"city":{"createdAt":"2019-10-22T07:06:38.913Z","id":"5d96e3d60c737f4ca185a4cb","latitude":17.375278,"longitude":78.474444,"name":"hyderabad","state":"maharashtra","status":1},"state":"Maharashtra","land_mark":"bsbshhshshh","secondary_number":9797997979,"user_id":"5da41692605227000724b902","primary_status":false,"status":false,"id":"5daeaa941021df00075282e1","createdAt":"2019-10-22T07:07:00.897Z","updatedAt":"2019-10-22T07:07:00.897Z"}]
     * message : default loopback api
     * status : success
     * statusCode : 200
     */

    private String message;
    private String status;
    private int statusCode;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : cjcjcjcjcjf
         * pincode : 868686
         * address_line1 : fjfjfjfjcjfjckc
         * address_line2 : fjfjdjfjfjfjfj
         * mobile_number : 6890906868
         * city : {"createdAt":"2019-10-22T07:00:36.344Z","id":"5d96e3d60c737f4ca185a4cb","latitude":17.375278,"longitude":78.474444,"name":"hyderabad","state":"maharashtra","status":1}
         * state : Maharashtra
         * land_mark : cjcjcjcjcd
         * secondary_number : 9898986868
         * user_id : 5da41692605227000724b902
         * primary_status : false
         * status : false
         * id : 5daea9232989630007b9c768
         * createdAt : 2019-10-22T07:00:51.438Z
         * updatedAt : 2019-10-22T07:00:51.438Z
         */

        private String name;
        private String pincode;
        private String address_line1;
        private String address_line2;
        private String mobile_number;
        private CityBean city;
        private String state;
        private String land_mark;
        private String secondary_number;
        private String user_id;
        private boolean primary_status;
        private boolean status;
        private String id;
        private String createdAt;
        private String updatedAt;

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

        public boolean isPrimary_status() {
            return primary_status;
        }

        public void setPrimary_status(boolean primary_status) {
            this.primary_status = primary_status;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
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

        public static class CityBean {
            /**
             * createdAt : 2019-10-22T07:00:36.344Z
             * id : 5d96e3d60c737f4ca185a4cb
             * latitude : 17.375278
             * longitude : 78.474444
             * name : hyderabad
             * state : maharashtra
             * status : 1
             */

            private String createdAt;
            private String id;
            private double latitude;
            private double longitude;
            private String name;
            private String state;
            private int status;

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

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

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
