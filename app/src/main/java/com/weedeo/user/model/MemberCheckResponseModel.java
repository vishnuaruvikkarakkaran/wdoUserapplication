package com.weedeo.user.model;

import java.util.List;

public class MemberCheckResponseModel {


    /**
     * data : []
     * message : This user not exist
     * status : success
     * statusCode : 200
     */

    private String message;
    private String status;
    private int statusCode;
    private List<?> data;

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

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
