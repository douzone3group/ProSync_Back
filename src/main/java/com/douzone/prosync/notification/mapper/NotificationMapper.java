package com.douzone.prosync.notification.mapper;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.notification.dto.NotificationDto;
import com.douzone.prosync.notification.dto.NotificationTargetDto;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.notification.entity.Notification;
import com.douzone.prosync.notification.entity.NotificationTarget;
import com.douzone.prosync.searchcondition.NotificationSearchCondition;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface NotificationMapper {
    void saveNotification(NotificationDto notificationDto);

    void saveNotificationTarget(NotificationTargetDto notificationDto);

    void saveNotificationTargetList(List<NotificationTargetDto> dtoList);

    void updateIsRead(@Param("isRead") boolean isRead,@Param("targetId") Long targetId);
    void updateIsTransmittedbyTagetId(@Param("isTransmitted") boolean isTransmitted,@Param("targetId") Long targetId);

    void updateIsTransmittedbyMemberId(@Param("isTransmitted") boolean isTransmitted,@Param("memberId") Long memberId);

    void update(NotificationDto notificationDto);

    void deleteNotificationTarget(Long targetId);

    void deleteNotification(Long notificationId);

    Notification findById(Long notificationId);

    List<NotificationResponse> getNotificationList(@Param("condition") NotificationSearchCondition condition);
    NotificationTarget findTargetById(Long targetId);

    Integer getNotificationListCount(Long memberId);

    List<NotificationTarget> getNotificationTargetListByNotificationId(Long notificationId);

    void deleteSchedulingTarget(Integer durationDate);

    void updateTargetListIsRead(List<Long> targetIds);

    void updateAllNotificationIsRead(Long memberId);

    void deleteTargetList(List<Long> targetIds);

    Integer getNotificationTargetListCount(@Param("targetIds") List<Long> targetIds,@Param("memberId")Long memberId);

    Integer getNotificationCountIsReadFalse(Long memberId);

    void deleteAllTarget(Long memberId);

}
