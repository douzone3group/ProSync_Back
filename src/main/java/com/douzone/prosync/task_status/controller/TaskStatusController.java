package com.douzone.prosync.task_status.controller;

import com.douzone.prosync.common.SingleResponseDto;
import com.douzone.prosync.task_status.dto.TaskStatusDto;
import com.douzone.prosync.task_status.service.TaskStatusService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
@Tag(name = "task_status", description = "업무 상태 API")
public class TaskStatusController {

    private final TaskStatusService taskStatusService;

    /**
     * 특정 프로젝트 업무 상태 생성
     * ADMIN, WRITER
     */
    @PostMapping("/projects/{project-id}/task-status")
    @Operation(summary = "업무 상태 생성", description = "특정 프로젝트의 업무 상태를 생성합니다.", tags = "task_status")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successfully retrieved", response = TaskStatusDto.SimpleResponseDto.class),
            @ApiResponse(code = 404, message = "project not found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity<SingleResponseDto<TaskStatusDto.SimpleResponseDto>> postTaskStatus(@Parameter(description = "프로젝트식별자", required = true, example = "1") @PathVariable("project-id") @Positive Long projectId,
                                                                                             @RequestBody @Valid TaskStatusDto.PostDto requestBody,
                                                                                             @Parameter(hidden = true) @ApiIgnore Principal principal) {
        Long taskStatusId = taskStatusService.createTaskStatus(projectId, requestBody, Long.parseLong(principal.getName()));
        return new ResponseEntity(new SingleResponseDto(new TaskStatusDto.SimpleResponseDto(taskStatusId)), HttpStatus.CREATED);
    }

    /**
     * 업무 상태 수정
     * ADMIN, WRITER
     */
    @PatchMapping("/task-status/{task-status-id}")
    @Operation(summary = "업무 상태 수정", description = "업무 상태를 수정합니다.", tags = "task_status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = TaskStatusDto.SimpleResponseDto.class),
            @ApiResponse(code = 404, message = "task_status_not_found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity<SingleResponseDto<TaskStatusDto.SimpleResponseDto>> patchTaskStatus(@Parameter(description = "업무상태식별자", required = true, example = "1") @PathVariable("task-status-id") @Positive Long taskStatusId,
                                                                                              @RequestBody @Valid TaskStatusDto.PatchDto requestBody,
                                                                                              @Parameter(hidden = true) @ApiIgnore Principal principal) {
        taskStatusService.updateTaskStatus(taskStatusId, requestBody, Long.parseLong(principal.getName()));
        return new ResponseEntity(new SingleResponseDto(new TaskStatusDto.SimpleResponseDto(taskStatusId)), HttpStatus.OK);
    }

    /**
     * 업무 상태 삭제
     * ADMIN, WRITER
     */
    @DeleteMapping("/task-status/{task-status-id}")
    @Operation(summary = "업무 상태 삭제", description = "업무 상태를 삭제합니다.", tags = "task_status")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "successfully retrieved"),
            @ApiResponse(code = 404, message = "task_status_not_found"),
            @ApiResponse(code = 409, message = "task_exists"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity deleteTaskStatus(@Parameter(description = "업무상태식별자", required = true, example = "1") @PathVariable("task-status-id") @Positive Long taskStatusId,
                                           @Parameter(hidden = true) @ApiIgnore Principal principal) {
        taskStatusService.deleteTaskStatus(taskStatusId, Long.parseLong(principal.getName()));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 프로젝트에 대한 모든 업무상태 조회
     * public - ALL USER
     * private - ADMIN, WRITER, READER
     */
    @GetMapping("/projects/{project-id}/task-status")
    @Operation(summary = "특정 프로젝트의 업무 상태 목록 조회", description = "특정 프로젝트의 업무 상태 목록을 전체 조회합니다. ", tags = "task_status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = TaskStatusDto.GetResponseDto.class),
            @ApiResponse(code = 404, message = "task_status_not_found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity<SingleResponseDto<List<TaskStatusDto.GetResponseDto>>> getProjectTaskStatus(@Parameter(description = "프로젝트 식별자", required = true, example = "1") @PathVariable("project-id") @Positive Long projectId,
                                                                                                      @Parameter(description = "보여짐 체크한 업무상태 조회", required = false, example = "true") @RequestParam(required = false) boolean isActive,
                                                                                                      @Parameter(hidden = true) @ApiIgnore Principal principal) {
        List<TaskStatusDto.GetResponseDto> response = taskStatusService.getTaskStatusByProject(projectId, isActive, getMemberId(principal));
        return new ResponseEntity(new SingleResponseDto(response), HttpStatus.OK);
    }

    /**
     * 업무상태 1건 조회
     * public - ALL USER
     * private - ADMIN, WRITER, READER
     */
    @GetMapping("/task-status/{task-status-id}")
    @Operation(summary = "업무 상태 조회", description = "업무상태식별자를 통해 업무 상태를 조회합니다.", tags = "task_status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = TaskStatusDto.GetResponseDto.class),
            @ApiResponse(code = 404, message = "task_status_not_found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity<SingleResponseDto<TaskStatusDto.GetResponseDto>> getTaskStatus(@Parameter(description = "업무상태식별자", required = true, example = "1") @PathVariable("task-status-id") @Positive Long taskStatusId,
                                                                                         @Parameter(hidden = true) @ApiIgnore Principal principal) {
        TaskStatusDto.GetResponseDto response = taskStatusService.getTaskStatus(taskStatusId, getMemberId(principal));
        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }


    private Long getMemberId(Principal principal) {
        return Optional.ofNullable(principal)
                .map(p -> Long.parseLong(p.getName()))
                .orElse(null);
    }
}
