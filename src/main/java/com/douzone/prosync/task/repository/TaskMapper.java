package com.douzone.prosync.task.repository;

import com.douzone.prosync.task.dto.request.TaskPatchDto;
import com.douzone.prosync.task.dto.request.TaskPostDto;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskMapper {

    void save(@Param("task") TaskPostDto dto, @Param("projectId") Integer projectId);

    @Select("SELECT * FROM task " +
            "LEFT JOIN task_status " +
            "ON task.task_status_id = task_status.task_status_id " +
            "WHERE task.task_id=#{taskId} and task.is_deleted IS NULL AND task_status.is_deleted IS NULL")
    Optional<GetTaskResponse> findById(Long taskId);

    @Delete("DELETE FROM task WHERE task_id=#{taskId}")
    void deleteById(Long taskId);

    //soft delete
    @Update("UPDATE task SET is_deleted=true WHERE task_id=#{taskId}")
    void delete(Long taskId);

    void update(TaskPatchDto dto);

    int findExistsTask(Long taskId);

    void saveTaskMember(@Param("taskId") Long taskId, @Param("memberIds") List<Long> memberIds);

    void deleteTaskMember(Long taskId, List<Long> memberIds);
}
