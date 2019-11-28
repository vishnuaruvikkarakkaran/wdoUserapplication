package com.weedeo.user.Utils;

public class MessageEvent {

    public final String name;
    public final String phone;
    public final String imageUrl;

    public MessageEvent(String name, String phone, String imageUrl) {
        this.name = name;
        this.phone = phone;
        this.imageUrl = imageUrl;
    }
}