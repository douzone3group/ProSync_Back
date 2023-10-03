package com.douzone.prosync.notification.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {
    SseEmitter save(Long memberId, SseEmitter sseEmitter);
    void deleteById(Long memberId);
    SseEmitter findById(Long memberId);
}
