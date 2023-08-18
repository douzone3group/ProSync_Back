package com.douzone.prosync.task.service;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.task.dto.request.TaskPatchDto;
import com.douzone.prosync.task.dto.request.TaskPostDto;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.dto.response.GetTasksResponse;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Integer createTask(TaskPostDto dto, Integer projectId, String userEmail);

    void updateTask(TaskPatchDto dto, Integer taskId, String userEmail);

    void deleteTask(Integer taskId, String userEmail);

    GetTaskResponse findTask(Integer taskId, String userEmail);

    PageResponseDto<GetTasksResponse> findTaskList(Integer projectId, Pageable pageable, String search, String userEmail);

}