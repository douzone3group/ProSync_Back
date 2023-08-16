package com.douzone.prosync.task.service;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.task.dto.request.TaskPatchDto;
import com.douzone.prosync.task.dto.request.TaskPostDto;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.dto.response.GetTasksResponse;
import com.douzone.prosync.task.entity.Task;
import com.douzone.prosync.task.repository.TaskJpaRepository;
import com.douzone.prosync.task.repository.TaskRepository;
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

    public Integer createTask(TaskPostDto dto, Integer projectId, String userEmail) {
        // TODO : 존재하는 project 인지 검증

        if (dto.getTaskStatus() == null || dto.getTaskStatus().isEmpty()) {
            dto.setTaskStatus("NO_STATUS");
        }
        LocalDateTime dateTime = LocalDateTime.now();
        dto.setCreatedAt(dateTime);

        return taskRepository.save(dto, projectId);
    }

    public void updateTask(TaskPatchDto dto, Integer taskId, String userEmail) {

        Task findTask = findExistTask(taskId);
        dto.setTaskId(taskId);

        if (Optional.ofNullable(dto.getClassification()).isEmpty()) {
            dto.setClassification(findTask.getClassification());
        }
        if (Optional.ofNullable(dto.getTitle()).isEmpty()) {
            dto.setTitle(findTask.getTitle());
        }
        if (Optional.ofNullable(dto.getDetail()).isEmpty()) {
            dto.setDetail(findTask.getDetail());
        }
        if (Optional.ofNullable(dto.getStartDate()).isEmpty()) {
            dto.setStartDate(findTask.getStartDate());
        }
        if (Optional.ofNullable(dto.getEndDate()).isEmpty()) {
            dto.setEndDate(findTask.getEndDate());
        }
        if (Optional.ofNullable(dto.getTaskStatus()).isEmpty()) {
            dto.setTaskStatus(findTask.getTaskStatus());
        }

        dto.setModifiedAt(LocalDateTime.now());
        //TODO : DB에 존재하는 task_status 인지 확인

        taskRepository.update(dto);
    }

    public void deleteTask(Integer taskId, String userEmail) {
        verifyExistTask(taskId);

        //soft delete
        taskRepository.delete(taskId);
    }

    @Transactional(readOnly = true)
    public GetTaskResponse findTask(Integer taskId, String userEmail) {
        Task findTask = findExistTask(taskId);
        return GetTaskResponse.of(findTask);
    }

    /**
     * 업무 리스트 조회
     */
    @Transactional(readOnly = true)
    public PageResponseDto<GetTasksResponse> findTaskList(Integer projectId, Pageable pageable, String search, String userEmail) {
        pageable = PageRequest.of(pageable.getPageNumber() == 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());

        Page<Task> pages = search != null && !search.trim().isEmpty() ?
                taskJpaRepository.findAllByProjectIdAndSearch(projectId, search, pageable) :
                taskJpaRepository.findByProjectIdAndIsDeletedNull(projectId, pageable);

        List<GetTasksResponse> res =
                pages.getContent()
                .stream()
                .map(GetTasksResponse::of)
                .collect(Collectors.toList());

        return new PageResponseDto<GetTasksResponse>(res, pages);
    }

    private Task findExistTask(Integer taskId) {
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
}
