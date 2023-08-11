package com.douzone.prosync.task.repository;

import com.douzone.prosync.task.dto.TaskRequest;
import com.douzone.prosync.task.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepository {

    private final TaskMybatisMapper taskMapper;

    public Integer save(TaskRequest.PostDto dto, Integer projectId) {
        taskMapper.save(dto, projectId);
        return dto.getTaskId();
    }

    public Optional<Task> findById(Integer taskId) {
        return taskMapper.findById(taskId);
    }


    public void deleteById(Integer taskId) {
        taskMapper.deleteById(taskId);
    }

    //soft delete
    public void delete(Integer taskId) {
        taskMapper.delete(taskId);
    }

    public void update(Task task) {
        taskMapper.update(task);
    }

    public boolean isDeletedTask(Integer taskId) {
        return taskMapper.findExistsTask(taskId) == 0;
    }

}
