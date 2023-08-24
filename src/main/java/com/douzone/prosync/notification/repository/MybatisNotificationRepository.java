package com.douzone.prosync.notification.repository;

import com.douzone.prosync.notification.dto.NotificationDto;
import com.douzone.prosync.notification.dto.NotificationTargetDto;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.notification.entity.Notification;
import com.douzone.prosync.notification.entity.NotificationTarget;
import com.douzone.prosync.notification.mapper.NotificationMapper;
import com.douzone.prosync.searchcondition.NotificationSearchCondition;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MybatisNotificationRepository implements NotificationRepository{

    private final NotificationMapper notificationMapper;
    @Override
    public Long saveNotification(NotificationDto notificationDto) {
        notificationMapper.saveNotification(notificationDto);
        return notificationDto.getNotificationId();
    }

    @Override
    public void saveNotificationTarget(NotificationTargetDto notificationDto) {
        notificationMapper.saveNotificationTarget(notificationDto);
    }

    @Override
    public void saveNotificationTargetList(List<NotificationTargetDto> dtoList) {
         notificationMapper.saveNotificationTargetList(dtoList);
    }

    @Override
    public void updateIsDeleted(boolean isDeleted) {

    }

    @Override
    public void updateIsRead(boolean isRead) {

    }

    @Override
    public void updateIsTransmittedbyTagetId(boolean isTransmitted, Long targetId) {
        notificationMapper.updateIsTransmittedbyTagetId(isTransmitted, targetId);
    }

    @Override
    public void updateIsTransmittedbyMemberId(boolean isTransmitted, Long memberId) {
        notificationMapper.updateIsTransmittedbyMemberId(isTransmitted, memberId);
    }

    @Override
    public void update(NotificationDto notificationDto) {

    }

    @Override
    public Optional<Notification> findById() {
        return Optional.empty();
    }

    @Override
    public List<NotificationResponse> getNotificationList(NotificationSearchCondition condition) {
        return notificationMapper.getNotificationList(condition);
    }

    @Override
    public List<NotificationTarget> getNotificationTagetListByNotificationId(Long notificationId) {
        return notificationMapper.getNotificationTagetListByNotificationId(notificationId);
    }

    @Override
    public Optional<NotificationTarget> findTargetById() {
        return Optional.empty();
    }

    @Override
    public Integer getNotificationListCount(Long memberId) {
        return notificationMapper.getNotificationListCount(memberId);
    }
}
