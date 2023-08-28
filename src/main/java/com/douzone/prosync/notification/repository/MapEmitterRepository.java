package com.douzone.prosync.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MapEmitterRepository implements EmitterRepository{

    private static ConcurrentHashMap<Long, SseEmitter> map = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(Long memberId, SseEmitter sseEmitter) {
        map.put(memberId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void deleteById(Long memberId) {
        map.remove(memberId);
    }

    @Override
    public SseEmitter findById(Long memberId) {
        return map.get(memberId);
    }
}
