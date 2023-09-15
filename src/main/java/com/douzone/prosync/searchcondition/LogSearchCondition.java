package com.douzone.prosync.searchcondition;

import com.douzone.prosync.log.logenum.LogCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogSearchCondition {

    private Long projectId;
    private LogCode logCode;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;


    public LogSearchCondition of(Long projectId, LogSearchCondition condition) {
        return new LogSearchCondition(projectId, condition.getLogCode(), condition.startDate, condition.getEndDate(), condition.getContent());

    }
}
