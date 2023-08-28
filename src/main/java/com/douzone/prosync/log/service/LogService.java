package com.douzone.prosync.log.service;

import com.douzone.prosync.log.dto.LogConditionDto;
import com.douzone.prosync.log.dto.request.LogPatchDto;
import com.douzone.prosync.log.dto.response.LogResponse;
import com.douzone.prosync.log.dto.response.LogSimpleResponse;
import com.douzone.prosync.searchcondition.LogSearchCondition;
import com.github.pagehelper.PageInfo;


public interface LogService {

    LogSimpleResponse saveLog(LogConditionDto dto);

    LogSimpleResponse deleteLog(Long logId);

    PageInfo<LogResponse> getLogList(LogSearchCondition condition);

    LogSimpleResponse updateLog(LogPatchDto dto);

    Integer getLogListCount(Long projectId);

}
