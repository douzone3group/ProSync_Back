package com.douzone.prosync.notification.service;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.notification.dto.NotificationConditionDto;
import com.douzone.prosync.notification.dto.request.NotificationListRequestDto;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.notification.dto.response.NotificationTargetSimpleResponse;
import com.douzone.prosync.notification.notienum.NotificationCode;
import com.douzone.prosync.searchcondition.NotificationSearchCondition;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface NotificationService {
    void send(Long memberId, Object data);

    void saveAndSendNotification(NotificationConditionDto dto);

     NotificationTargetSimpleResponse updateNotificationIsRead(Long targetId, Long memberId);

     PageResponseDto<NotificationResponse> getNotificationPageList(NotificationListRequestDto requestDto, Pageable pageable, Principal principal);

    List<NotificationTargetSimpleResponse> updateTargetListIsRead(List<Long> notificationTargetIds, Long memberId);

    List<NotificationTargetSimpleResponse> deleteTargetList(List<Long> notificationTargetIds, Long memberId);
}
