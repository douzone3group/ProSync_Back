package com.douzone.prosync.task.repository;

import com.douzone.prosync.task.dto.request.TaskMemberResponseDto;
import com.douzone.prosync.task.dto.request.TaskPatchDto;
import com.douzone.prosync.task.dto.request.TaskPostDto;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.dto.response.GetTasksResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskMapper {

    void save(@Param("task") TaskPostDto dto, @Param("projectId") Long projectId);

    Optional<GetTaskResponse> findById(Long taskId);

    @Delete("DELETE FROM task WHERE task_id=#{taskId}")
    void deleteById(Long taskId);

    //soft delete
    @Update("UPDATE task SET is_deleted=true, modified_at=now() WHERE task_id=#{taskId}")
    void delete(Long taskId);

    void update(TaskPatchDto dto);

    void saveTaskMember(@Param("taskId") Long taskId, @Param("projectMemberIds") List<Long> projectMemberIds);

    void deleteTaskMember(@Param("taskId") Long taskId, @Param("projectMemberIds") List<Long> projectMemberIds);

    List<TaskMemberResponseDto> findTaskMembers(Long taskId);


    List<GetTasksResponse> findTasks(@Param("projectId") Long projectId, @Param("search") String search, @Param("isActive") Boolean isActive);
}


