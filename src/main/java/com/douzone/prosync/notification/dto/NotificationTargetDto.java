package com.douzone.prosync.notification.dto;

import com.douzone.prosync.notification.notienum.NotificationPlatform;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationTargetDto {
    // PK
    private Long notificationTargetId;
    private Long notificationId;
    private Long memberId;
    private boolean isRead;
    private Long updateUserId;
    private boolean isTransmitted;


    private NotificationPlatform platform;
}
