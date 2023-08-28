package com.douzone.prosync.notification.notienum;

public enum NotificationPlatform {

    WEB("웹"), EMAIL("이메일");


    final private String platform;

    NotificationPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }
}
