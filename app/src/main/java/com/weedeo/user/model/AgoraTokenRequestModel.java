package com.weedeo.user.model;

public class AgoraTokenRequestModel {


    /**
     * channel : channel name
     * uid : user_id
     */

    private String channel;
    private String uid;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
