package com.douzone.prosync.task.repository;

import com.douzone.prosync.task.dto.request.TaskPatchDto;
import com.douzone.prosync.task.dto.request.TaskPostDto;
import com.douzone.prosync.task.entity.Task;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface TaskMybatisMapper {

    void save(@Param("task") TaskPostDto dto, @Param("projectId") Integer projectId);

    @Select("select * from task where task_id=#{taskId} and is_deleted is null")
    Optional<Task> findById(Integer taskId);

    @Delete("delete from task where task_id=#{taskId}")
    void deleteById(Integer taskId);

    //soft delete
    @Update("update task set is_deleted=true where task_id=#{taskId}")
    void delete(Integer taskId);

    void update(TaskPatchDto dto);

    int findExistsTask(Integer taskId);
}
