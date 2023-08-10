package com.douzone.prosync.task.repository;

import com.douzone.prosync.task.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepository {

    private final TaskMybatisMapper taskMapper;

    public Task save(Task task, Integer projectId) {
        taskMapper.save(task, projectId);
        return task;
    }

    public void updateTask(Task task) {
        taskMapper.update(task);
    }

    public Optional<Task> findById(Integer taskId) {
        return taskMapper.findById(taskId);
    }

    public void deleteById(Integer taskId) {
        taskMapper.deleteById(taskId);
    }

    public List<Task> findAllByProject(Integer projectId, int offset, int size, String search) {
        return taskMapper.findTasksWithPagination(projectId, offset, size, search);
    }

    public int findTaskCount(Integer projectId, String search) {
        return taskMapper.findTaskCount(projectId, search);
    }

    public boolean isDeletedTask(Integer taskId) {
        return taskMapper.findExistsTask(taskId) == 0;
    }

    //soft delete
    public void delete(Integer taskId) {
        taskMapper.delete(taskId);
    }
}
