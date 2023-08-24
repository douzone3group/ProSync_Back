package com.douzone.prosync.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    void send(Long memberId, Object data);

}
