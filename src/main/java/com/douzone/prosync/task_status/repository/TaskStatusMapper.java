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
    @Update("UPDATE task_status SET is_deleted=true WHERE task_status_id=#{taskStatusId}")
    void delete(Integer taskStatusId);

    List<TaskStatusDto.GetResponseDto> findTaskStatusByProject(Integer projectId, boolean isActive);

    @Select("SELECT task_status_id, color, task_status, seq " +
            "FROM task_status " +
            "WHERE task_status_id=#{taskStatusId} AND is_deleted IS NULL")
    Optional<TaskStatusDto.GetResponseDto> findTaskStatus(Integer taskStatusId);

    int findExistsTaskStatus(Integer taskStatusId);

}
