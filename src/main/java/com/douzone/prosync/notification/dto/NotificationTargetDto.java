package com.douzone.prosync.notification.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationTargetDto {
    // PK
    private Long notificationTargetId;
    private Long notificationId;
    private Long memberId;
    private boolean isRead;
    private boolean isDeleted;
    private Long updateUserId;
    private boolean isTransmitted;


    // TODO:플랫폼 enum 설정하기
    private String platform;
}
