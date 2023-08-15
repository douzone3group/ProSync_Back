package com.douzone.prosync.task.controller;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.task.dto.request.TaskPatchDto;
import com.douzone.prosync.task.dto.request.TaskPostDto;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.dto.response.TaskSimpleResponse;
import com.douzone.prosync.task.service.TaskServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Positive;


//TODO : token 완료 후 인증 로직 추가
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class TaskController {

    private final TaskServiceImpl taskService;

    /**
     * 업무 생성
     */
    @PostMapping("/projects/{project-id}/tasks")
    @Operation(summary = "업무 생성", description = "프로젝트에 대한 업무를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successfully retrieved", response = TaskSimpleResponse.class),
            @ApiResponse(code = 404, message = "project not found"),
            @ApiResponse(code = 500, message = "server error")
    })
    public ResponseEntity postTask(@Parameter(description = "업무식별자", required = true, example = "1") @PathVariable("project-id") @Positive Integer projectId,
                                   @RequestBody @Valid TaskPostDto requestBody) {
        Integer taskId = taskService.createTask(requestBody, projectId, null);
        return new ResponseEntity<>(new TaskSimpleResponse(taskId), HttpStatus.CREATED);
    }

    /**
     * 업무 수정
     */
    @PatchMapping("/tasks/{task-id}")
    @Operation(summary ="업무 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = TaskSimpleResponse.class),
            @ApiResponse(code = 404, message = "task not found"),
            @ApiResponse(code = 500, message = "server error")
    })
    public ResponseEntity patchTask(@Parameter(description = "업무식별자", required = true, example = "1") @PathVariable("task-id") @Positive Integer taskId,
                                    @RequestBody @Valid TaskPatchDto requestBody) {
        taskService.updateTask(requestBody, taskId, null);
        return new ResponseEntity<>(new TaskSimpleResponse(taskId), HttpStatus.OK);
    }

    /**
     * 업무 삭제
     */
    @DeleteMapping("/tasks/{task-id}")
    @Operation(summary = "업무 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "successfully retrieved", response = TaskSimpleResponse.class),
            @ApiResponse(code = 404, message = "task not found"),
            @ApiResponse(code = 500, message = "server error")
    })
    public ResponseEntity deleteTask(@Parameter(description = "업무식별자", required = true, example = "1") @PathVariable("task-id") @Positive Integer taskId) {
        taskService.deleteTask(taskId, null);
        return new ResponseEntity<>(new TaskSimpleResponse(taskId), HttpStatus.NO_CONTENT);
    }

    /**
     * 프로젝트 업무 리스트 조회
     * @param projectId : 프로젝트 식별자
     * @param search : 검색 키워드
     * @param pageable : page - 조회할 페이지 번호, size - 한페이지에 보여질 요소 개수
     * @return 프로젝트 한건에 해당하는 업무 리스트를 페이지 형식으로 리턴합니다.
     */
    @GetMapping("/projects/{project-id}/tasks")
    @Operation(summary ="프로젝트 업무 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = PageResponseDto.class),
            @ApiResponse(code = 500, message = "server error")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "조회할 페이지 번호", defaultValue = "1", example = "1"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "한페이지에 보여질 요소 개수", defaultValue = "10", example = "20")
    })
    public ResponseEntity getTaskList(@Parameter(description = "프로젝트 식별자", required = true, example = "1") @PathVariable("project-id") @Positive Integer projectId,
                                      @Parameter(description = "검색 키워드", example = "홍길동") @RequestParam(required = false) String search,
                                      @Parameter(hidden = true) @ApiIgnore @PageableDefault(sort = "taskId", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponseDto pageResponseDto = taskService.findTaskList(projectId, pageable, search, null);
        return new ResponseEntity<>(pageResponseDto, HttpStatus.OK);
    }

    /**
     * 업무 상세 조회
     */
    @GetMapping("/tasks/{task-id}")
    @Operation(summary ="업무 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = GetTaskResponse.class),
            @ApiResponse(code = 404, message = "task not found"),
            @ApiResponse(code = 500, message = "server error")
    })
    public ResponseEntity getTask(@Parameter(description = "프로젝트 식별자", required = true, example = "1") @PathVariable("task-id") Integer taskId) {
        return new ResponseEntity<>(taskService.findTask(taskId, null), HttpStatus.OK);
    }

}
