package com.douzone.prosync.log.dto;

import com.douzone.prosync.log.logenum.LogCode;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LogConditionDto {

    private Long fromMemberId;

    private LogCode code;

    private List<Long> memberIds;

    private Long projectId;

    private Long taskId;

    private List<Long> existProjectIds;

    private Object subject;

}
