package com.weedeo.user.model;

import java.util.List;

public class CityResponseModel {

    /**
     * data : [{"name":"hyderabad","state":"maharashtra","status":"1","latitude":"17.375278","longitude":"78.474444","id":"5d96e3d60c737f4ca185a4cb","createdAt":"2019-10-22T05:09:04.555Z"}]
     * message : City list successfully received
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
         * name : hyderabad
         * state : maharashtra
         * status : 1
         * latitude : 17.375278
         * longitude : 78.474444
         * id : 5d96e3d60c737f4ca185a4cb
         * createdAt : 2019-10-22T05:09:04.555Z
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
