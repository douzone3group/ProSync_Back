package com.douzone.prosync.notification.dto.response;

import com.douzone.prosync.notification.notienum.NotificationCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NotificationResponse {
    private Long notificationTargetId;
    private boolean isRead;
    private String content;
    private NotificationCode code;
    private LocalDateTime createdAt;
    private String url;
}
