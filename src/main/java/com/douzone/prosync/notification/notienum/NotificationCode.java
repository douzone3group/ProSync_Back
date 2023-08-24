package com.douzone.prosync.notification.notienum;

public enum NotificationCode {
    TASK("업무"), PROJECT("프로젝트"), COMMENT("댓글");


    final private String code;

    NotificationCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
