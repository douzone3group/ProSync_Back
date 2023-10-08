package com.douzone.prosync.log.scheduler;

import com.douzone.prosync.log.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.douzone.prosync.constant.ConstantPool.LOG_EXPIRATION_DURATION;

@Component
public class LogCleanUpScheduler {

    @Autowired
    private LogMapper mapper;

    // 1일 마다 실행
    @Scheduled(fixedRate = 1L* 24 * 60 * 60 *1000)
    public void cleanUpNotification() {

        // 3년이 지난 로그들은 삭제 처리
        LocalDateTime daysAgo = LocalDateTime.now().minusDays(LOG_EXPIRATION_DURATION);
        mapper.cleanUpLog(daysAgo);

    }
}
