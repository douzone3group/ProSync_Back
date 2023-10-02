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

    void save(@Param("projectId") Long projectId, @Param("taskStatus") TaskStatusDto.PostDto dto);

    void update(TaskStatusDto.PatchDto dto);

    @Update("UPDATE task_status " +
            "SET seq = seq + 1 " +
            "WHERE project_id = #{projectId} AND seq >= #{seq} AND seq < 100 AND is_deleted is false AND task_status_id != #{taskStatusId}")
    void updateSeq(@Param("projectId") Long projectId, @Param("seq") Integer seq, @Param("taskStatusId") Long taskStatusId);

    //soft delete
    @Update("UPDATE task_status SET is_deleted=true, modified_at=now() WHERE task_status_id=#{taskStatusId}")
    void delete(Long taskStatusId);

    List<TaskStatusDto.GetResponseDto> findTaskStatusByProject(Long projectId, boolean isActive);

    @Select("SELECT task_status_id, color, task_status, seq, project_id " +
            "FROM task_status " +
            "WHERE task_status_id=#{taskStatusId} AND is_deleted IS false")
    Optional<TaskStatusDto.GetResponseDto> findTaskStatus(Long taskStatusId);

    int findExistsTaskStatus(Long taskStatusId);

    int findTaskByTaskStatus(Long taskStatusId);
}
