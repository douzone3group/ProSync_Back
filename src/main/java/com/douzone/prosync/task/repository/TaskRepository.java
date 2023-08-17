package com.douzone.prosync.task.repository;

import com.douzone.prosync.task.dto.request.TaskPatchDto;
import com.douzone.prosync.task.dto.request.TaskPostDto;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepository {

    private final TaskMybatisMapper taskMapper;

    public Integer save(TaskPostDto dto, Integer projectId) {
        taskMapper.save(dto, projectId);
        return dto.getTaskId();
    }

    public Optional<GetTaskResponse> findById(Integer taskId) {
        return taskMapper.findById(taskId);
    }

    public void deleteById(Integer taskId) {
        taskMapper.deleteById(taskId);
    }

    //soft delete
    public void delete(Integer taskId) {
        taskMapper.delete(taskId);
    }

    public void update(TaskPatchDto dto) {
        taskMapper.update(dto);
    }

    public boolean isDeletedTask(Integer taskId) {
        return taskMapper.findExistsTask(taskId) == 0;
    }

}
