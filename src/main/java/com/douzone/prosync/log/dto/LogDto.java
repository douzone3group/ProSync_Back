package com.douzone.prosync.log.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LogDto {

    private Long logId;

    private LocalDateTime createdAt;

    private String content;

    private boolean isDeleted;

    private Long projectId;

    private LocalDateTime modifiedAt;

    private String logCode;

    private String url;


}
