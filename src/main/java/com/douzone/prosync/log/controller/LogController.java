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
import org.springframework.web.bind.annotation.*;

import static com.douzone.prosync.constant.ConstantPool.PAGE_NAVI;


@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
// Todo: Log 로직 작성하기
public class LogController {

    private final LogService logService;

    private final LogRepository logRepository;


    /**
     * 로그 삭제하기
     * ADMIN
     */
    @DeleteMapping("/projectlog/{project-id}/{log-id}")
    public ResponseEntity<LogSimpleResponse> deleteLog(@PathVariable("project-id") Long projectId, @PathVariable("log-id") Long logId) {
        LogSimpleResponse logSimpleResponse = logService.deleteLog(logId);
        return new ResponseEntity<>(logSimpleResponse, HttpStatus.OK);
    }


    /**
     * 로그 목록 조회
     * ADMIN
     */
    @GetMapping("/projectlog/{project-id}")
    public PageInfo<LogResponse> getLogList(@PathVariable("project-id") Long projectId, LogSearchCondition condition){

        LogSearchCondition logSearchCondition = condition.of(projectId, condition);
        return new PageInfo<>(logRepository.getLogList(logSearchCondition), PAGE_NAVI);

    }

    /**
     * 로그 업데이트 하기
     * ADMIN
     */
    @PatchMapping("/projectlog/{project-id}/{log-id}")
    public ResponseEntity updateLog(@PathVariable("log-id") Long logId, LogPatchDto dto){

        LogSimpleResponse logSimpleResponse= logService.updateLog(logId,dto);
        return new ResponseEntity<>(logSimpleResponse, HttpStatus.OK);
    }

    /**
     * 로그 갯수 조회
     * ADMIN
     */
    @GetMapping("/projectlog/{project-id}/count")
    public ResponseEntity getLogListCount(@PathVariable("project-id") Long projectId){
        return new ResponseEntity<>(logRepository.getLogListCount(projectId), HttpStatus.OK);
    }
}
