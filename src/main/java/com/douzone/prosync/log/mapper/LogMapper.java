package com.douzone.prosync.log.mapper;

import com.douzone.prosync.log.dto.LogDto;
import com.douzone.prosync.log.dto.request.LogPatchDto;
import com.douzone.prosync.log.dto.response.LogResponse;
import com.douzone.prosync.searchcondition.LogSearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface LogMapper {

    void saveLog(LogDto logDto);




    void update(@Param("logId") Long logId,@Param("logPatchDto")LogPatchDto logPatchDto);


    void deleteLog(@Param("logId")Long logId,@Param("modifiedAt") LocalDateTime modifiedAt);

    LogResponse findById(Long logId);

    List<LogResponse> getLogList(@Param("condition") LogSearchCondition condition);

    Integer getLogListCount(@Param("projectId") Long projectId);




}
