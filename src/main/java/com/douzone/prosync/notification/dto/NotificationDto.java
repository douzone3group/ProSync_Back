package com.douzone.prosync.notification.dto;

import com.douzone.prosync.notification.notienum.NotificationCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationDto {

    // PK
    private Long notificationId;

    private NotificationCode code;

    private Long fromMemberId;


    private String content;
    private String url;

}
