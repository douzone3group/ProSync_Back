package com.douzone.prosync.task.service;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.task.dto.request.TaskPatchDto;
import com.douzone.prosync.task.dto.request.TaskPostDto;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.dto.response.GetTasksResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    Integer createTask(TaskPostDto dto, Integer projectId, Long memberId);

    void updateTask(TaskPatchDto dto, Integer taskId, Long memberId);

    void deleteTask(Integer taskId, Long memberId);

    GetTaskResponse findTask(Integer taskId, Long memberId);

    PageResponseDto<GetTasksResponse.PerTasksResponse> findTaskList(Integer projectId, Pageable pageable, String search, boolean isActive, Long memberId);

    void createTaskMember(Integer taskId, List<Long> memberIds, Long memberId);

    void deleteTaskMember(Integer taskId, List<Long> memberIds, Long memberId);

}