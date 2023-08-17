package com.douzone.prosync.task_status.service;

import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.task_status.dto.TaskStatusDto;
import com.douzone.prosync.task_status.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//TODO : 권한가진 프로젝트회원인지 검증
@Service
@Transactional
@RequiredArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    public Integer createTaskStatus(Integer projectId, TaskStatusDto.PostDto requestBody) {
        return taskStatusRepository.save(projectId, requestBody);
    }

    public void updateTaskStatus(Integer taskStatusId, TaskStatusDto.PatchDto requestBody) {
        requestBody.setTaskStatusId(taskStatusId);
        verifyExistTaskStatus(taskStatusId);
        taskStatusRepository.update(requestBody);
    }

    public void deleteTaskStatus(Integer taskStatusId) {
        verifyExistTaskStatus(taskStatusId);
        taskStatusRepository.delete(taskStatusId);
    }

    public List<TaskStatusDto.GetResponseDto> getTaskStatusByProject(Integer projectId, boolean isActive) {
        return taskStatusRepository.findTaskStatusByProject(projectId, isActive);
    }

    public TaskStatusDto.GetResponseDto getTaskStatus(Integer taskStatusId) {
        return taskStatusRepository.findTaskStatus(taskStatusId).orElseThrow(() -> new ApplicationException(ErrorCode.TASK_STATUS_NOT_FOUND));
    }

    private void verifyExistTaskStatus(Integer taskStatusId) {
        if (taskStatusRepository.isDeletedTask(taskStatusId)) {
                throw new ApplicationException(ErrorCode.TASK_STATUS_NOT_FOUND);
        }
    }
}
