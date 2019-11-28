package com.weedeo.user.model;

public class AgoraTokenResponseModel {

    /**
     * data : {"accessToken":"006e9083a06af214c29a75562b62e1b1833IAAsWIY1ui9PtJVbynkk5J7GIag/GxC/HFnay/vt/N6hYGaq9bmV026nIgAuQ5nAbK/DXQQAAQAAAAAAAgAAAAAAAwAAAAAABAAAAAAA","signalingToken":"1:e9083a06af214c29a75562b62e1b1833:1573105516:b9e146b406e17173fcf6bd336cfd64ad"}
     * message : Token generated successfully
     * status : success
     * statusCode : 200
     */

    private DataBean data;
    private String message;
    private String status;
    private int statusCode;

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

    public static class DataBean {
        /**
         * accessToken : 006e9083a06af214c29a75562b62e1b1833IAAsWIY1ui9PtJVbynkk5J7GIag/GxC/HFnay/vt/N6hYGaq9bmV026nIgAuQ5nAbK/DXQQAAQAAAAAAAgAAAAAAAwAAAAAABAAAAAAA
         * signalingToken : 1:e9083a06af214c29a75562b62e1b1833:1573105516:b9e146b406e17173fcf6bd336cfd64ad
         */

        private String accessToken;
        private String signalingToken;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getSignalingToken() {
            return signalingToken;
        }

        public void setSignalingToken(String signalingToken) {
            this.signalingToken = signalingToken;
        }
    }
}
