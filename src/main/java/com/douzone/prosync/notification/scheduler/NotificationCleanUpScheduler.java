package com.douzone.prosync.notification.scheduler;

import com.douzone.prosync.notification.mapper.NotificationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.douzone.prosync.constant.ConstantPool.NOTIFICATION_EXPIRATION_DURATION;

@Component
@Slf4j
public class NotificationCleanUpScheduler {


    @Autowired
    private NotificationMapper mapper;

    // 1일 마다 실행
    @Scheduled(fixedRate = 1L * 24 * 60 * 60 * 1000)
    public void cleanUpNotification() {
        // 180일 지난 알림들은 삭제 처리
        LocalDateTime daysAgo = LocalDateTime.now().minusDays(NOTIFICATION_EXPIRATION_DURATION);
        mapper.cleanUpNotificationTarget(daysAgo);
        mapper.cleanUpNotification(daysAgo);
        log.info("알림 스케줄링 실행 완료");
    }
}
