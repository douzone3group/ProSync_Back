package com.douzone.prosync.log.dto.request;

import com.douzone.prosync.log.logenum.LogCode;
import com.douzone.prosync.searchcondition.LogSearchCondition;

import java.time.LocalDateTime;

public class LogListRequestDto {

    private LogCode logCode;
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String content;

    private Integer pageNum;

    private Integer pageSize;

//    public LogSearchCondition of(Long projectId){
//        return LogSearchCondition.builder()
//                .projectId(projectId)
//                .content(content)
//                .startDate(startDate)
//                .endDate(endDate)
//                .logCode(logCode)
//                .build();
//
//    }
}
