package com.douzone.prosync.notification.service;

import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.notification.dto.response.NotificationData;
import com.douzone.prosync.notification.repository.MapEmitterRepository;
import com.douzone.prosync.notification.repository.MybatisNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static com.douzone.prosync.constant.ConstantPool.*;

@RequiredArgsConstructor
@Service
public class WebNotificationServiceImpl implements NotificationService{

    private final MapEmitterRepository emitterRepositoryrepository;
    private final MybatisNotificationRepository notificationRepository;
    private void sendToClient(SseEmitter sseEmitter, Object data) {

        try {
            sseEmitter.send(SseEmitter.event()
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            System.out.println("SSE 연결 에러 발생");
            throw new RuntimeException("SSE 연결 오류 발생");
        }
    }



    public SseEmitter subscribe(Long memberId) {
        SseEmitter emitter = emitterRepositoryrepository.save(memberId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepositoryrepository.deleteById(memberId));
        emitter.onTimeout(() -> emitterRepositoryrepository.deleteById(memberId));
        emitter.onError((e) -> emitterRepositoryrepository.deleteById(memberId));

        Integer count = notificationRepository.getNotificationListCount(memberId);

        try {
            sendToClient(emitter,new NotificationData("안읽으신 "+count+"개의 메시지가 있습니다.",FRONT_SERVER_HOST+"/notifications"));
            notificationRepository.updateIsTransmittedbyMemberId(true,memberId);
        } catch (RuntimeException e){
            throw new ApplicationException(ErrorCode.CONNECTION_ERROR);
        }

        return emitter;
    }

    @Override
    public void send(Long memberId, Object data) {
        SseEmitter emitter = emitterRepositoryrepository.findById(memberId);
        sendToClient(emitter,data);
    }
}
