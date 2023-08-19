package com.douzone.prosync.task_status.service;

import com.douzone.prosync.task_status.dto.TaskStatusDto;

import java.util.List;

public interface TaskStatusService {

    Integer createTaskStatus(Integer projectId, TaskStatusDto.PostDto requestBody, Long memberId);

    void updateTaskStatus(Integer taskStatusId, TaskStatusDto.PatchDto requestBody, Long memberId);

    void deleteTaskStatus(Integer taskStatusId, Long memberId);

    List<TaskStatusDto.GetResponseDto> getTaskStatusByProject(Integer projectId, boolean isActive, Long memberId);

    TaskStatusDto.GetResponseDto getTaskStatus(Integer taskStatusId, Long memberId);

}