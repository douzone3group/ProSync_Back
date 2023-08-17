package com.douzone.prosync.task.service;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.project.service.ProjectService;
import com.douzone.prosync.task.dto.request.TaskPatchDto;
import com.douzone.prosync.task.dto.request.TaskPostDto;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.dto.response.GetTasksResponse;
import com.douzone.prosync.task.entity.Task;
import com.douzone.prosync.task.repository.TaskJpaRepository;
import com.douzone.prosync.task.repository.TaskRepository;
import com.douzone.prosync.task_status.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskJpaRepository taskJpaRepository;
    private final TaskStatusService taskStatusService;
    private final ProjectService projectService;

    public Integer createTask(TaskPostDto dto, Integer projectId, String userEmail) {
        // TODO : 존재하는 project 인지 + 프로젝트 회원 맞는지?
        Project findProject = projectService.findProject(projectId);

        // check task_status of project
        verifyTaskStatus(findProject.getProjectId(), dto.getTaskStatusId());

        dto.setCreatedAt(LocalDateTime.now());

        return taskRepository.save(dto, projectId);
    }

    public void updateTask(TaskPatchDto dto, Integer taskId, String userEmail) {

        GetTaskResponse findTask = findExistTask(taskId);
        dto.setTaskId(taskId);
        dto.setModifiedAt(LocalDateTime.now());

        // find task_status of project
        if (Optional.ofNullable(dto.getTaskStatusId()).isPresent()) {
            verifyTaskStatus(findTask.getProjectId(), dto.getTaskStatusId());
        }

        taskRepository.update(dto);
    }

    public void deleteTask(Integer taskId, String userEmail) {
        verifyExistTask(taskId);

        //soft delete
        taskRepository.delete(taskId);
    }

    @Transactional(readOnly = true)
    public GetTaskResponse findTask(Integer taskId, String userEmail) {
        return findExistTask(taskId);
    }

    /**
     * 업무 리스트 조회
     */
    @Transactional(readOnly = true)
    public PageResponseDto<GetTasksResponse.PerTasksResponse> findTaskList(Integer projectId, Pageable pageable, String search, boolean isActive, String userEmail) {
        pageable = PageRequest.of(pageable.getPageNumber() == 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());

        Page<Task> pages = search != null && !search.trim().isEmpty() ?
                taskJpaRepository.findAllByProjectIdAndSearch(projectId, search, pageable) :
                taskJpaRepository.findByProjectIdAndIsDeletedNull(projectId, pageable);

        // isActive = true면 체크된 업무만 조회
        List<GetTasksResponse> res = isActive ? pages.getContent()
                .stream()
                .map(task -> GetTasksResponse.of(task))
                .filter(task -> task.getSeq() != 0)
                .collect(Collectors.toList()) :
                pages.getContent()
                .stream()
                .map(task -> GetTasksResponse.of(task))
                .collect(Collectors.toList());

        return new PageResponseDto(GetTasksResponse.PerTasksResponse.of(res), pages);
    }

    private GetTaskResponse findExistTask(Integer taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new ApplicationException(ErrorCode.TASK_NOT_FOUND));
    }

    /**
     * 삭제된 task인 경우 예외 처리
     */
    private void verifyExistTask(Integer taskId) {
        if (taskRepository.isDeletedTask(taskId)) {
            throw new ApplicationException(ErrorCode.TASK_NOT_FOUND);
        }
    }

    private void verifyTaskStatus(Integer projectId, Integer taskStatusId) {
        taskStatusService.getTaskStatusByProject(projectId, false)
                .stream()
                .filter(status -> status.getTaskStatusId() == taskStatusId).findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.TASK_STATUS_NOT_FOUND));
    }

}
