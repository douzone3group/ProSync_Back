package com.douzone.prosync.notification.dto.response;

public class NotificationData {

    public String content;
    public String url;

    public NotificationResponse notificationResponse;

    public boolean isCountMessage;

    public NotificationData(String content, String url) {
        this.content=content;
        this.url=url;
        this.isCountMessage=true;
    }

    public NotificationData(NotificationResponse response) {
        this.notificationResponse = response;
        this.isCountMessage=false;
    }



}
