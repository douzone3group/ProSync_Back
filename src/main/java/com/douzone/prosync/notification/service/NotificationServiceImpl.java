package com.douzone.prosync.notification.service;

import com.douzone.prosync.constant.ConstantPool;
import com.douzone.prosync.notification.repository.MapEmitterRepository;
import com.douzone.prosync.notification.repository.MybatisNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static com.douzone.prosync.constant.ConstantPool.*;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService{

    private final MapEmitterRepository emitterRepositoryrepository;
    private final MybatisNotificationRepository notificationRepository;
    @Override
    public void sendToClient(SseEmitter sseEmitter, Object data) {

        try {
            sseEmitter.send(SseEmitter.event()
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(boolean isTransmitted, Long memberId){ //
        notificationRepository.updateIsTransmitted(true, memberId);
    }

    @Override
    public SseEmitter subscribe(Long memberId) {
        SseEmitter emitter = emitterRepositoryrepository.save(memberId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepositoryrepository.deleteById(memberId));
        emitter.onTimeout(() -> emitterRepositoryrepository.deleteById(memberId));
        emitter.onError((e) -> emitterRepositoryrepository.deleteById(memberId));

        return emitter;
    }
}
