package com.douzone.prosync.log.dto;

import com.douzone.prosync.log.logenum.LogCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LogDto {

    private Long logId;


    private String content;

    private boolean isDeleted;

    private Long projectId;

    private LocalDateTime modifiedAt;

    private LogCode logCode;

    private String url;


}
