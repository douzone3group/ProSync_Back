package com.douzone.prosync.task_status.service;

import com.douzone.prosync.task_status.dto.TaskStatusDto;

import java.util.List;

public interface TaskStatusService {

    Integer createTaskStatus(Integer projectId, TaskStatusDto.PostDto requestBody);

    void updateTaskStatus(Integer taskStatusId, TaskStatusDto.PatchDto requestBody);

    void deleteTaskStatus(Integer taskStatusId);

    List<TaskStatusDto.GetResponseDto> getTaskStatusByProject(Integer projectId, boolean isActive);

    TaskStatusDto.GetResponseDto getTaskStatus(Integer taskStatusId);

}