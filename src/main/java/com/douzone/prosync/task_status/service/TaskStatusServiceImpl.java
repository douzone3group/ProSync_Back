package com.douzone.prosync.task_status.service;

import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.task_status.dto.TaskStatusDto;
import com.douzone.prosync.task_status.repository.TaskStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//TODO : 권한가진 프로젝트회원인지 검증
@Service
@Transactional
@RequiredArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusMapper taskStatusMapper;

    public Integer createTaskStatus(Long projectId, TaskStatusDto.PostDto requestBody, Long memberId) {
        taskStatusMapper.save(projectId, requestBody);
        return requestBody.getTaskStatusId();
    }

    public void updateTaskStatus(Integer taskStatusId, TaskStatusDto.PatchDto requestBody, Long memberId) {
        requestBody.setTaskStatusId(taskStatusId);
        verifyExistTaskStatus(taskStatusId);
        taskStatusMapper.update(requestBody);
    }

    public void deleteTaskStatus(Integer taskStatusId, Long memberId) {
        verifyExistTaskStatus(taskStatusId);
        taskStatusMapper.delete(taskStatusId);
    }

    public List<TaskStatusDto.GetResponseDto> getTaskStatusByProject(Long projectId, boolean isActive, Long memberId) {
        return taskStatusMapper.findTaskStatusByProject(projectId, isActive);
    }

    public TaskStatusDto.GetResponseDto getTaskStatus(Integer taskStatusId, Long memberId) {
        return taskStatusMapper.findTaskStatus(taskStatusId).orElseThrow(() -> new ApplicationException(ErrorCode.TASK_STATUS_NOT_FOUND));
    }

    private void verifyExistTaskStatus(Integer taskStatusId) {
        if (taskStatusMapper.findExistsTaskStatus(taskStatusId) == 0) {
                throw new ApplicationException(ErrorCode.TASK_STATUS_NOT_FOUND);
        }
    }
}
