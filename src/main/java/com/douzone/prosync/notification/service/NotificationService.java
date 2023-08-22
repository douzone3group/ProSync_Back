package com.douzone.prosync.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    void sendToClient(SseEmitter sseEmitter, Object data);
    SseEmitter subscribe(Long memberId);

    void send(boolean isTransmitted, Long memberId);
}
