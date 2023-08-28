package com.douzone.prosync.log.repository;

import com.douzone.prosync.log.dto.LogDto;
import com.douzone.prosync.log.dto.request.LogPatchDto;
import com.douzone.prosync.log.dto.response.LogResponse;
import com.douzone.prosync.log.dto.response.LogSimpleResponse;
import com.douzone.prosync.log.mapper.LogMapper;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.searchcondition.LogSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MybatisLogRepository implements LogRepository{

    private final LogMapper logMapper;

    @Override
    public Long saveLog(LogDto dto) {
        logMapper.saveLog(dto);
        return dto.getLogId();
    }

//    @Override
//    public Long saveMemberLog(Long memberId, Long logId) {
//        return null;
//    }

    @Override
    public Long deleteLog(Long logId) {
        logMapper.deleteLog(logId, LocalDateTime.now());
        return logId;
    }

    @Override
    public List<LogResponse> getLogList(LogSearchCondition condition) {
       return logMapper.getLogList(condition);
    }

    @Override
    public Long updateLog(LogPatchDto dto) {
        logMapper.update(dto);
        return dto.getLogId();
    }

    @Override
    public LogResponse findById(Long logId) {
        return logMapper.findById(logId);
    }

    @Override
    public Integer getLogListCount(Long projectId) {
        return logMapper.getLogListCount(projectId);
    }
}
