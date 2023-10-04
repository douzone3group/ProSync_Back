package com.douzone.prosync.task.controller;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.common.SingleResponseDto;
import com.douzone.prosync.member.dto.response.MemberGetResponse;
import com.douzone.prosync.task.dto.request.TaskMemberRequestDto;
import com.douzone.prosync.task.dto.request.TaskMemberResponseDto;
import com.douzone.prosync.task.dto.request.TaskPatchDto;
import com.douzone.prosync.task.dto.request.TaskPostDto;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.dto.response.GetTasksResponse;
import com.douzone.prosync.task.dto.response.TaskSimpleResponse;
import com.douzone.prosync.task.service.TaskService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import static com.douzone.prosync.constant.ConstantPool.DEFAULT_PAGE_SIZE;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
@Slf4j
@Tag(name = "task", description = "업무 API")
public class TaskController {

    private final TaskService taskService;

    /**
     * 업무 생성
     * ADMIN, WRITER
     */
    @PostMapping("/projects/{project-id}/tasks/task-status/{task-status-id}")
    @Operation(summary = "업무 생성", description = "프로젝트에 대한 신규 업무를 생성합니다.", tags = "task")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successfully retrieved", response = TaskSimpleResponse.class),
            @ApiResponse(code = 404, message = "project not found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity<SingleResponseDto<TaskSimpleResponse>> postTask(@Parameter(description = "프로젝트식별자", required = true, example = "1") @PathVariable("project-id") @Positive Long projectId,
                                                                          @Parameter(description = "업무상태식별자", required = true, example = "1") @PathVariable("task-status-id") @Positive Long taskStatusId,
                                                                          @RequestBody @Valid TaskPostDto requestBody,
                                                                          @Parameter(hidden = true) @ApiIgnore Principal principal) {
        requestBody.setTaskStatusId(taskStatusId);
        Long taskId = taskService.createTask(requestBody, projectId, Long.parseLong(principal.getName()));
        return new ResponseEntity<>(new SingleResponseDto(new TaskSimpleResponse(taskId)), HttpStatus.CREATED);
    }

    /**
     * 업무 수정
     * ADMIN, WRITER
     */
    @PatchMapping("/tasks/{task-id}")
    @Operation(summary = "업무 수정", description = "특정 업무를 수정합니다.", tags = "task")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = TaskSimpleResponse.class),
            @ApiResponse(code = 404, message = "task not found"),
            @ApiResponse(code = 500, message = "server error")
    })
    public ResponseEntity<SingleResponseDto<TaskSimpleResponse>> patchTask(@Parameter(description = "업무식별자", required = true, example = "1") @PathVariable("task-id") @Positive Long taskId,
                                                                           @RequestBody @Valid TaskPatchDto requestBody,
                                                                           @Parameter(hidden = true) @ApiIgnore Principal principal) {
        taskService.updateTask(requestBody, taskId, Long.parseLong(principal.getName()));
        return new ResponseEntity<>(new SingleResponseDto(new TaskSimpleResponse(taskId)), HttpStatus.OK);
    }

    /**
     * 업무 삭제
     * ADMIN, WRITER
     */
    @DeleteMapping("/tasks/{task-id}")
    @Operation(summary = "업무 삭제", description = "특정 업무를 삭제합니다.", tags = "task")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "successfully retrieved", response = TaskSimpleResponse.class),
            @ApiResponse(code = 404, message = "task not found"),
            @ApiResponse(code = 500, message = "server error")
    })
    public ResponseEntity deleteTask(@Parameter(description = "업무식별자", required = true, example = "1") @PathVariable("task-id") @Positive Long taskId,
                                     @Parameter(hidden = true) @ApiIgnore Principal principal) {
        taskService.deleteTask(taskId, Long.parseLong(principal.getName()));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 프로젝트 업무 리스트 조회 - ok
     * public - LOGIN USER
     * private - ADMIN, WRITER, READER
     */
    @GetMapping("/projects/{project-id}/tasks")
    @Operation(summary = "프로젝트 업무 목록 조회", description = "프로젝트 목록을 페이지네이션으로 조회합니다. 검색창으로 통해 프로젝트를 검색할 수 있습니다.", tags = "task")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = PageResponseDto.class),
            @ApiResponse(code = 500, message = "server error")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "조회할 페이지 번호", defaultValue = "1", example = "1"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "한페이지에 보여질 요소 개수", defaultValue = "10", example = "20"),
            @ApiImplicitParam(name = "search", dataType = "string", paramType = "query", value = "검색키워드", example = "제목/작성자/업무상태"),
            @ApiImplicitParam(name = "isActive", dataType = "boolean", paramType = "query", value = "체크한 업무 보임 여부", example = "true"),
            @ApiImplicitParam(name = "view", dataType = "string", paramType = "query", value = "보드뷰 - 업무상태별 응답 출력", example = "board"),
            @ApiImplicitParam(name = "status", dataType = "string", paramType = "query", value = "업무상태", example = "todo")
    })
    public ResponseEntity<PageResponseDto<GetTasksResponse>> getTaskList(@Parameter(description = "업무식별자", required = true, example = "1") @PathVariable("project-id") @Positive Long projectId,
                                                                         @RequestParam(required = false) String search,
                                                                         @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                         @RequestParam(required = false, defaultValue = "10") Integer size,
                                                                         @RequestParam(required = false) boolean isActive,
                                                                         @RequestParam(required = false) String view,
                                                                         @RequestParam(required = false) String status,
                                                                         @Parameter(hidden = true) @ApiIgnore Principal principal) {
        PageResponseDto pageResponseDto = taskService.findTaskList(projectId, page, size, search, isActive, view, status, getMemberId(principal));
        return new ResponseEntity<>(pageResponseDto, HttpStatus.OK);
    }

    /**
     * 업무 상세 조회 - ok
     * public - ALL USER
     * private - ADMIN, WRITER, READER
     */
    @GetMapping("/tasks/{task-id}")
    @Operation(summary = "업무 상세 조회", description = "특정 업무을 조회합니다.", tags = "task")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = GetTaskResponse.class),
            @ApiResponse(code = 404, message = "task not found"),
            @ApiResponse(code = 500, message = "server error")
    })
    public ResponseEntity<GetTaskResponse.TaskMembersDto> getTask(@Parameter(description = "업무식별자", required = true, example = "1") @PathVariable("task-id") Long taskId,
                                                                      @Parameter(hidden = true) @ApiIgnore Principal principal) {
        Long memberId = getMemberId(principal);
        GetTaskResponse task = taskService.findTask(taskId, memberId);
        List<TaskMemberResponseDto> taskMembers = taskService.findTaskMembers(taskId, memberId);
        return new ResponseEntity<>(new GetTaskResponse.TaskMembersDto(task, taskMembers), HttpStatus.OK);
    }

    /**
     * 업무 담당자 (복수) 설정
     * ADMIN, WRITER
     */
    @PostMapping("/tasks/{task-id}/members")
    @Operation(summary = "업무 담당자 지정", description = "특정 업무에 대한 담당자들을 지정합니다.", tags = "task")
    public ResponseEntity<SingleResponseDto<TaskSimpleResponse>> postTaskMember(@Parameter(description = "업무식별자", required = true, example = "1") @PathVariable("task-id") @Positive Long taskId,
                                                                                @RequestBody TaskMemberRequestDto requestBody,
                                                                                @Parameter(hidden = true) @ApiIgnore Principal principal) {
        taskService.createTaskMember(taskId, requestBody.getProjectMemberIds(), Long.parseLong(principal.getName()));
        return new ResponseEntity(new SingleResponseDto<>(new TaskSimpleResponse(taskId)), HttpStatus.OK);
    }

    /**
     * 업무 담당자 삭제 (DB 삭제)
     * ADMIN, WRITER
     */
    @DeleteMapping("/tasks/{task-id}/members")
    @Operation(summary = "업무 담당자 삭제", description = "특정 업무에 대한 담당자들을 삭제합니다.", tags = "task")
    public ResponseEntity<SingleResponseDto<TaskSimpleResponse>> deleteTaskMember(@Parameter(description = "업무식별자", required = true, example = "1") @PathVariable("task-id") @Positive Long taskId,
                                                                                  @RequestBody TaskMemberRequestDto requestBody,
                                                                                  @Parameter(hidden = true) @ApiIgnore Principal principal) {
        taskService.deleteTaskMember(taskId, requestBody.getProjectMemberIds(), Long.parseLong(principal.getName()));
        return new ResponseEntity(new SingleResponseDto<>(new TaskSimpleResponse(taskId)), HttpStatus.OK);
    }

    /**
     * 업무 담당자 목록 조회
     * public - ALL USER
     * private - ADMIN, WRITER, READER
     */
    @GetMapping("/tasks/{task-id}/members")
    @Operation(summary = "업무 담당자 목록 조회", description = "특정 업무에 대한 담당자를 전체 조회합니다.", tags = "task")
    public ResponseEntity<SingleResponseDto<List<MemberGetResponse.SimpleResponse>>> getTaskMember(@Parameter(description = "업무식별자", required = true, example = "1") @PathVariable("task-id") @Positive Long taskId,
                                                                                                   @Parameter(hidden = true) @ApiIgnore Principal principal) {
        List<TaskMemberResponseDto> res = taskService.findTaskMembers(taskId, getMemberId(principal));
        return new ResponseEntity(new SingleResponseDto<>(res), HttpStatus.OK);
    }

    private Long getMemberId(Principal principal) {
        return Optional.ofNullable(principal)
                .map(p -> Long.parseLong(p.getName()))
                .orElse(null);
    }
}
