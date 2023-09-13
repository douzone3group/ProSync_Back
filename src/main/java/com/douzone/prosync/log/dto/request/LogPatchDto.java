package com.douzone.prosync.log.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LogPatchDto {


    private String content;

    private LocalDateTime modifiedAt;

    private String logCode;

    private String url;
}
