package com.douzone.prosync.task.service;

import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.task.dto.TaskRequest;
import com.douzone.prosync.task.entity.Task;
import com.douzone.prosync.task.repository.TaskJpaRepository;
import com.douzone.prosync.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceV1 implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskJpaRepository taskJpaRepository;

    public Integer createTask(TaskRequest.PostDto dto, Integer projectId, String userEmail) {
        if (dto.getTaskStatus() == null) {
            dto.setTaskStatus("NO_STATUS");
        }
        return taskRepository.save(dto, projectId);
    }

    public void updateTask(TaskRequest.PatchDto dto, String userEmail) {

        Task findTask = findExistTask(dto.getTaskId());
        Optional.ofNullable(dto.getClassification())
                .ifPresent(findTask::setClassification);
        Optional.ofNullable(dto.getTitle())
                .ifPresent(findTask::setTitle);
        Optional.ofNullable(dto.getDetail())
                .ifPresent(findTask::setDetail);
        Optional.ofNullable(dto.getStartDate())
                .ifPresent(findTask::setStartDate);
        Optional.ofNullable(dto.getEndDate())
                .ifPresent(findTask::setEndDate);
        Optional.ofNullable(dto.getTaskStatus())
                .ifPresent(findTask::setTaskStatus);

        taskRepository.update(findTask);
    }

    public void deleteTask(Integer taskId, String userEmail) {
        verifyExistTask(taskId);

        //soft delete
        taskRepository.delete(taskId);
//        taskRepository.deleteById(taskId);
    }

    @Transactional(readOnly = true)
    public Task findTask(Integer taskId, String userEmail) {
        return findExistTask(taskId);
    }

    /**
     * 업무 리스트 조회
     */
    @Transactional(readOnly = true)
    public Page<Task> findTaskList(Integer projectId, Pageable pageable, String search, String userEmail) {
        pageable = PageRequest.of(pageable.getPageNumber() == 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        if (search != null && !search.trim().isEmpty()) {
            return taskJpaRepository.findAllByProjectIdAndSearch(projectId, search, pageable);
        }
        return taskJpaRepository.findByProjectIdAndIsDeletedNull(projectId, pageable);
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
