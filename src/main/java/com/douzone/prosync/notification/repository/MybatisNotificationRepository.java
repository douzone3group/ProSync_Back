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
    public void saveNotification(NotificationDto notificationDto) {
        notificationMapper.saveNotification(notificationDto);
    }

    @Override
    public void saveNotificationTarget(NotificationTargetDto notificationDto) {

    }

    @Override
    public void updateIsDeleted(boolean isDeleted) {

    }

    @Override
    public void updateIsRead(boolean isRead) {

    }

    @Override
    public void updateIsTransmitted(boolean isTransmitted, Long memberId) {
        notificationMapper.updateIsTransmitted(isTransmitted, memberId);
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
    public Optional<NotificationTarget> findTargetById() {
        return Optional.empty();
    }
}
