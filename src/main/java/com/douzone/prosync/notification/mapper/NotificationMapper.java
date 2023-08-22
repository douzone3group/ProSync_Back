package com.douzone.prosync.notification.mapper;

import com.douzone.prosync.notification.dto.NotificationDto;
import com.douzone.prosync.notification.dto.NotificationTargetDto;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.notification.entity.Notification;
import com.douzone.prosync.notification.entity.NotificationTarget;
import com.douzone.prosync.searchcondition.NotificationSearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface NotificationMapper {
    void saveNotification(NotificationDto notificationDto);

    void saveNotificationTarget(NotificationTargetDto notificationDto);

    void updateIsDeleted(@Param("isDeleted") boolean isDeleted, @Param("targetId") Long targetId);
    void updateIsRead(@Param("isRead") boolean isRead,@Param("targetId") Long targetId);
    void updateIsTransmitted(@Param("isTransmitted") boolean isTransmitted,@Param("targetId") Long memberId);

    void update(NotificationDto notificationDto);

    Optional<Notification> findById(Long notificationId);

    List<NotificationResponse> getNotificationList(@Param("condition") NotificationSearchCondition condition);
    Optional<NotificationTarget> findTargetById(Long targetId);
}
