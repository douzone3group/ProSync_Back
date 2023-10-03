package com.douzone.prosync.log.controller;


import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.log.dto.request.LogPatchDto;
import com.douzone.prosync.log.dto.response.LogResponse;
import com.douzone.prosync.log.dto.response.LogSimpleResponse;
import com.douzone.prosync.log.logenum.LogCode;
import com.douzone.prosync.log.repository.LogRepository;
import com.douzone.prosync.log.service.LogService;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.project.dto.response.GetProjectsResponse;
import com.douzone.prosync.project.service.ProjectService;
import com.douzone.prosync.searchcondition.LogSearchCondition;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

import static com.douzone.prosync.constant.ConstantPool.DEFAULT_PAGE_SIZE;
import static com.douzone.prosync.constant.ConstantPool.PAGE_NAVI;


@RestController
@Slf4j
@Tag(name = "log", description = "로그 API")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
// Todo: Log 로직 작성하기
public class LogController {

    private final LogService logService;

    private final LogRepository logRepository;

    private final ProjectService projectService;

    /**
     * 로그 삭제하기
     * ADMIN
     */
    @Operation(summary = "로그 삭제", description = "로그를 삭제한다", tags = "log")
    @DeleteMapping("/projectlog/{project-id}/{log-id}")
    public ResponseEntity<LogSimpleResponse> deleteLog(@PathVariable("project-id") Long projectId,
                                                       @Parameter(example = "1", description = "로그 식별자", required = true) @PathVariable("log-id") Long logId) {
        LogSimpleResponse logSimpleResponse = logService.deleteLog(logId);
        return new ResponseEntity<>(logSimpleResponse, HttpStatus.OK);
    }


    /**
     * 로그 목록 조회
     * ADMIN
     */
    @Operation(summary = "로그 목록 조회", description = "로그 목록을 조회한다", tags = "log")
    @GetMapping("/projectlog/{project-id}")
    public ResponseEntity<PageResponseDto<LogResponse>> getLogList(@Parameter(example = "1", description = "프로젝트 식별자", required = true)
                                                                    @PathVariable("project-id") Long projectId,
                                                                    @RequestParam(required = false) LogCode code,
                                                                   @RequestParam(required = false) LocalDateTime startDate,
                                                                   @RequestParam(required = false) LocalDateTime endDate,
                                                                   @RequestParam(required = false) String content,
                                                                   @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable){

        LogSearchCondition condition = new LogSearchCondition(projectId, code, startDate, endDate, content);

        PageResponseDto<LogResponse> logPageList = logService.getLogPageList(condition, pageable);



        return new ResponseEntity<>(logPageList, HttpStatus.OK);

    }

    /**
     * 로그 업데이트 하기
     * ADMIN
     */
    @Operation(summary = "로그 업데이트", description = "업데이트된 로그를 반환하여 보여준다", tags = "log")
    @PatchMapping("/projectlog/{project-id}/{log-id}")
    public ResponseEntity updateLog(@Parameter(example = "1", description = "로그 식별자", required = true) @PathVariable("log-id") Long logId, LogPatchDto dto){

        LogSimpleResponse logSimpleResponse= logService.updateLog(logId,dto);
        return new ResponseEntity<>(logSimpleResponse, HttpStatus.OK);
    }

    /**
     * 로그 갯수 조회
     * ADMIN
     */
    @Operation(summary = "로그 갯수 조회", description = "로그의 총 갯수를 조회함", tags = "log")
    @GetMapping("/projectlog/{project-id}/count")
    public ResponseEntity getLogListCount(@Parameter(example = "1", description = "프로젝트 식별자", required = true) @PathVariable("project-id") Long projectId){
        return new ResponseEntity<>(logRepository.getLogListCount(projectId), HttpStatus.OK);
    }


    /**
     * ADMIN인 프로젝트 조회
     */
    @Operation(summary = "ADMIN인 프로젝트  조회", description = "ADMIN 권한을 가진 프로젝트들을 조회함", tags = "log")
    @GetMapping("/projectlog/admin")
    public ResponseEntity getMyProjectsPartOfAdmin(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable,
                                                   @Parameter(hidden = true) Principal principal) {

        PageResponseDto<GetProjectsResponse> projectPageList = projectService.findMyProjectsPartOfAdmin(Long.parseLong(principal.getName()), pageable);
        return new ResponseEntity<>(projectPageList, HttpStatus.OK);

    }


}
