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
}
