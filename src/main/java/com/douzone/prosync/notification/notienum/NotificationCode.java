package com.douzone.prosync.notification.notienum;

public enum NotificationCode {
    TASKASSIGNMENT("업무"), PULL("풀리퀘스트"), COMMIT("커밋");


    final private String code;

    NotificationCode(String code) {
        this.code = code;
    }

    String getCode() {
        return code;
    }
}
