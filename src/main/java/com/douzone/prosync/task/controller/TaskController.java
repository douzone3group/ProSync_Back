package com.douzone.prosync.task.controller;

import com.douzone.prosync.common.PageInfo;
import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.task.dto.TaskRequest;
import com.douzone.prosync.task.entity.Task;
import com.douzone.prosync.task.mapper.TaskMapper;
import com.douzone.prosync.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;


//TODO : token 완료 후 인증 로직 추가
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper mapper;

    /**
     * 업무 생성
     */
    @PostMapping("/projects/{project-id}/tasks")
    public ResponseEntity postTask(@PathVariable("project-id") Integer projectId, @RequestBody @Valid TaskRequest.PostDto dto) {
        Task task = taskService.createTask(mapper.postDtoToTask(dto), null);
        return new ResponseEntity<>(mapper.taskToSimpleResponse(task), HttpStatus.CREATED);
    }

    /**
     * 업무 수정
     */
    @PatchMapping("/tasks/{task-id}")
    public ResponseEntity patchTask(@PathVariable("task-id") Integer taskId,
                                    @RequestBody @Valid TaskRequest.PatchDto dto) {
        dto.setTaskId(taskId);
        taskService.updateTask(mapper.patchDtoToTask(dto), null);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 업무 삭제
     */
    @DeleteMapping("/tasks/{task-id}")
    public ResponseEntity deleteTask(@PathVariable("task-id") Integer taskId) {
        taskService.deleteTask(taskId, null);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 프로젝트 업무 리스트 조회
     * @param projectId : 프로젝트 식별자
     * @param page : 조회할 페이지 번호
     * @param size : 페이지당 보여질 요소 개수
     * @return 프로젝트 한건에 해당하는 업무 리스트를 페이지 형식으로 리턴합니다.
     * //TODO: 필터링추가
     */
    @GetMapping("/projects/{project-id}/tasks")
    public ResponseEntity getTaskList(@PathVariable("project-id") Integer projectId, @RequestParam @Positive int page, @RequestParam @Positive int size) {
        Map<String, Object> map = taskService.findTaskList(projectId, page-1, size, null);
        List<Task> tasks = (List<Task>) map.get("tasks");
        PageInfo pageInfo = (PageInfo) map.get("pageInfo");
        return new ResponseEntity<>(new PageResponseDto<>(mapper.tasksToGetTaskResponse(tasks), pageInfo), HttpStatus.OK);
    }

    /**
     * 업무 상세 조회
     */
    @GetMapping("/tasks/{task-id}")
    public ResponseEntity getTask(@PathVariable("task-id") Integer taskId) {
        Task task = taskService.findTask(taskId, null);
        return new ResponseEntity<>(mapper.taskToGetTaskResponse(task), HttpStatus.OK);
    }
}
