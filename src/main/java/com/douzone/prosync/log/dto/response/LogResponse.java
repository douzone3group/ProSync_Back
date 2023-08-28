package com.douzone.prosync.log.dto.response;

import com.douzone.prosync.log.logenum.LogCode;
import com.douzone.prosync.notification.notienum.NotificationCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LogResponse {
    private Long logId;
    private String content;
    private LogCode code;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String url;
}
