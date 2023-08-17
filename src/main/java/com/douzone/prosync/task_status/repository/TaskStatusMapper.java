package com.douzone.prosync.task_status.repository;

import com.douzone.prosync.task_status.dto.TaskStatusDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskStatusMapper {

    void save(@Param("projectId") Integer projectId, @Param("taskStatus") TaskStatusDto.PostDto dto);

    void update(TaskStatusDto.PatchDto dto);

    //soft delete
    @Update("update task_status set is_deleted=true where task_status_id=#{taskStatusId}")
    void delete(Integer taskStatusId);

    List<TaskStatusDto.GetResponseDto> findTaskStatusByProject(Integer projectId, boolean isActive);

    @Select("select task_status_id, color, task_status, seq " +
            "from task_status " +
            "where task_status_id=#{taskStatusId} and is_deleted is null")
    Optional<TaskStatusDto.GetResponseDto> findTaskStatus(Integer taskStatusId);

    int findExistsTaskStatus(Integer taskStatusId);

}
