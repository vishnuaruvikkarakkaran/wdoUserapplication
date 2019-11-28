package com.weedeo.user.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CitySearchResponse {


    /**
     * data : [{"name":"thiruvananthapuram","state":"kerala","status":"1","latitude":"8.506944","longitude":"76.956944","id":"5d96e3910c737f4ca185a4ca","createdAt":"2019-10-18T06:55:19.231Z"}]
     * message : City list successfully received
     * status : success
     * statusCode : 200
     */

    private String message;
    private String status;
    private int statusCode;
    @SerializedName("data")
    private List<Cities> city;

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

    public List<Cities> getCity() {
        return city;
    }

    public void setCity(List<Cities> city) {
        this.city = city;
    }

    public static class Cities {
        /**
         * name : thiruvananthapuram
         * state : kerala
         * status : 1
         * latitude : 8.506944
         * longitude : 76.956944
         * id : 5d96e3910c737f4ca185a4ca
         * createdAt : 2019-10-18T06:55:19.231Z
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
