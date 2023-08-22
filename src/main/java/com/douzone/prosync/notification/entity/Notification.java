package com.douzone.prosync.notification.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
public class Notification {
    private Long notificationId;
    private String notiCode;
    private Long fromMemberId;
    private LocalDateTime insertTime;
    private String content;
    private String url;
}
