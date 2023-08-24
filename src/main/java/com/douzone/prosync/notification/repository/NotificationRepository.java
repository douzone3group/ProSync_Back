package com.douzone.prosync.notification.repository;

import com.douzone.prosync.member.dto.MemberDto;
import com.douzone.prosync.member.dto.request.MemberPatchProfileDto;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.notification.dto.NotificationDto;
import com.douzone.prosync.notification.dto.NotificationTargetDto;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.notification.entity.Notification;
import com.douzone.prosync.notification.entity.NotificationTarget;
import com.douzone.prosync.searchcondition.NotificationSearchCondition;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface NotificationRepository {
    Long saveNotification(NotificationDto notificationDto);

    void saveNotificationTarget(NotificationTargetDto notificationDto);

    void saveNotificationTargetList(List<NotificationTargetDto> dtoList);
    void updateIsDeleted(boolean isDeleted);
    void updateIsRead(boolean isRead);
    void updateIsTransmittedbyTagetId(boolean isTransmitted, Long targetId);
    void updateIsTransmittedbyMemberId(boolean isTransmitted, Long memberId);

    void update(NotificationDto notificationDto);

    Optional<Notification> findById();
    List<NotificationResponse> getNotificationList(NotificationSearchCondition condition);

    List<NotificationTarget> getNotificationTagetListByNotificationId(Long notificationId);
    Optional<NotificationTarget> findTargetById();

    Integer getNotificationListCount(Long memberId);
}
