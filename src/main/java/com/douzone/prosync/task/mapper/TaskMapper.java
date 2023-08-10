package com.douzone.prosync.task.mapper;

import com.douzone.prosync.task.dto.TaskRequest;
import com.douzone.prosync.task.dto.TaskResponse;
import com.douzone.prosync.task.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    Task postDtoToTask(TaskRequest.PostDto dto);

    Task patchDtoToTask(TaskRequest.PatchDto dto);

    TaskResponse.SimpleResponse taskToSimpleResponse(Task task);

    TaskResponse.GetTaskResponse taskToGetTaskResponse(Task task);

    List<TaskResponse.GetTaskResponse> tasksToGetTaskResponse(List<Task> tasks);
}
