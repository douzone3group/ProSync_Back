package com.douzone.prosync.task.service;

import com.douzone.prosync.task.dto.TaskRequest;
import com.douzone.prosync.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Integer createTask(TaskRequest.PostDto dto, Integer projectId, String userEmail);

    void updateTask(TaskRequest.PatchDto dto, String userEmail);

    void deleteTask(Integer taskId, String userEmail);

    Task findTask(Integer taskId, String userEmail);

    Page<Task> findTaskList(Integer projectId, Pageable pageable, String search, String userEmail);

}