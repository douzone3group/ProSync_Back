package com.douzone.prosync.notification.repository;

import com.douzone.prosync.notification.dto.NotificationDto;
import com.douzone.prosync.notification.dto.NotificationTargetDto;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.notification.entity.Notification;
import com.douzone.prosync.notification.entity.NotificationTarget;
import com.douzone.prosync.searchcondition.NotificationSearchCondition;

import java.util.List;


public interface NotificationRepository {
    Long saveNotification(NotificationDto notificationDto);

    void saveNotificationTarget(NotificationTargetDto notificationDto);

    void saveNotificationTargetList(List<NotificationTargetDto> dtoList);

    void updateIsRead(boolean isRead, Long targetId);
    void updateIsTransmittedbyTagetId(boolean isTransmitted, Long targetId);
    void updateIsTransmittedbyMemberId(boolean isTransmitted, Long memberId);

    void update(NotificationDto notificationDto);

    void deleteNotificationTarget(Long targetId);

    void deleteNotification(Long notificationId);

    Notification findById(Long notificationId);
    List<NotificationResponse> getNotificationList(NotificationSearchCondition condition);

    List<NotificationTarget> getNotificationTagetListByNotificationId(Long notificationId);

    NotificationTarget findTargetById(Long notificationTargetId);

    Integer getNotificationListCount(Long memberId);

    void deleteSchedulingTarget(Integer durationDate);
}
