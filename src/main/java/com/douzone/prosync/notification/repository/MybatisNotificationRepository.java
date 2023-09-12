package com.douzone.prosync.notification.repository;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.notification.dto.NotificationDto;
import com.douzone.prosync.notification.dto.NotificationTargetDto;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.notification.entity.Notification;
import com.douzone.prosync.notification.entity.NotificationTarget;
import com.douzone.prosync.notification.mapper.NotificationMapper;
import com.douzone.prosync.searchcondition.NotificationSearchCondition;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public void updateIsRead(boolean isRead, Long targetId) {
        notificationMapper.updateIsRead(isRead, targetId);
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
        notificationMapper.update(notificationDto);
    }

    @Override
    public void deleteNotificationTarget(Long targetId) {
        notificationMapper.deleteNotificationTarget(targetId);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        notificationMapper.deleteNotification(notificationId);
    }

    @Override
    public Notification findById(Long notificationId) {
        return notificationMapper.findById(notificationId);
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
    public NotificationTarget findTargetById(Long notificationTargetId) {
        return notificationMapper.findTargetById(notificationTargetId);
    }

    @Override
    public Integer getNotificationListCount(Long memberId) {
        return notificationMapper.getNotificationListCount(memberId);
    }


    /**
     * 알림의 만료기간이 지난 알림을 스케줄링하여 삭제할 때 사용되는 로직
     * @param durationDate 만료기간
     */
    @Override
    public void deleteSchedulingTarget(Integer durationDate) {
        notificationMapper.deleteSchedulingTarget(durationDate);
    }
}
