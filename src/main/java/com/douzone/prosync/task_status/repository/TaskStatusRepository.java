package com.douzone.prosync.task_status.repository;

import com.douzone.prosync.task_status.dto.TaskStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskStatusRepository {

    private final TaskStatusMapper taskStatusMapper;

    public Integer save(Integer projectId, TaskStatusDto.PostDto dto) {
        taskStatusMapper.save(projectId, dto);
        return dto.getTaskStatusId();
    }

    public void update(TaskStatusDto.PatchDto dto) {
        taskStatusMapper.update(dto);
    }

    public void delete(Integer taskStatusId) {
        taskStatusMapper.delete(taskStatusId);
    }

    public List<TaskStatusDto.GetResponseDto> findTaskStatusByProject(Integer projectId, boolean isActive) {
        return taskStatusMapper.findTaskStatusByProject(projectId, isActive);
    }

    public Optional<TaskStatusDto.GetResponseDto> findTaskStatus(Integer taskStatusId) {
        return taskStatusMapper.findTaskStatus(taskStatusId);
    }

    public boolean isDeletedTask(Integer taskStatusId) {
        return taskStatusMapper.findExistsTaskStatus(taskStatusId) == 0;
    }

}
