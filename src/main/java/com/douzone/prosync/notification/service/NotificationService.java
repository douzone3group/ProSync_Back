package com.douzone.prosync.notification.service;

import com.douzone.prosync.notification.notienum.NotificationCode;

import java.util.List;

public interface NotificationService {
    void send(Long memberId, Object data);

    void saveAndSendNotification(Long fromMemberId, NotificationCode code, Object subject, List<Long> memberIds);

}
