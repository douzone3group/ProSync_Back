package com.douzone.prosync.searchcondition;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class LogSearchCondition {

    private Long projectId;
    private String logCode;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;

    public LogSearchCondition of(Long projectId, LogSearchCondition condition) {
        return LogSearchCondition.builder()
                .projectId(projectId)
                .logCode(condition.logCode)
                .startDate(condition.startDate)
                .endDate(condition.endDate)
                .content(condition.content).build();
    }
}
