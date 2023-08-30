package com.douzone.prosync.log.controller;


import com.douzone.prosync.log.dto.request.LogPatchDto;
import com.douzone.prosync.log.dto.response.LogResponse;
import com.douzone.prosync.log.dto.response.LogSimpleResponse;
import com.douzone.prosync.log.repository.LogRepository;
import com.douzone.prosync.log.service.LogService;
import com.douzone.prosync.searchcondition.LogSearchCondition;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.douzone.prosync.constant.ConstantPool.PAGE_NAVI;


@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
// Todo: Log 로직 작성하기
public class LogController {

    private final LogService logService;

    private final LogRepository logRepository;


    @DeleteMapping("/projectlog/{id}")
    public ResponseEntity<LogSimpleResponse> deleteLog(@PathVariable("id") Long logId) {
        LogSimpleResponse logSimpleResponse = logService.deleteLog(logId);
        return new ResponseEntity<>(logSimpleResponse, HttpStatus.OK);
    }


    public PageInfo<LogResponse> getLogList(LogSearchCondition condition){

        return new PageInfo<>(logRepository.getLogList(condition), PAGE_NAVI);

    }


    public LogSimpleResponse updateLog(LogPatchDto dto){

        Long id = logRepository.updateLog(dto);
        return new LogSimpleResponse(id);
    }


    public Integer getLogListCount(Long projectId){
        return logRepository.getLogListCount(projectId);
    }
}
