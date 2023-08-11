package com.douzone.prosync.task.mapper;

import com.douzone.prosync.task.dto.TaskResponse;
import com.douzone.prosync.task.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    TaskResponse.SimpleResponse taskToSimpleResponse(Integer taskId);

    TaskResponse.GetTaskResponse taskToGetTaskResponse(Task task);

    List<TaskResponse.GetTasksResponse> tasksToGetTasksResponse(List<Task> tasks);
}