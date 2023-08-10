package com.douzone.prosync.task.repository;

import com.douzone.prosync.task.entity.Task;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskMybatisMapper {

    void save(@Param("task") Task task, @Param("projectId") Integer projectId);

    @Select("select * from task where task_id=#{taskId} and is_deleted is null")
    Optional<Task> findById(Integer taskId);


    List<Task> findTasksWithPagination(@Param("projectId") Integer projectId,
                                       @Param("offset") int offset,
                                       @Param("size") int size,
                                       @Param("search") String search);

    @Delete("delete from task where task_id=#{taskId}")
    void deleteById(Integer taskId);

    //soft delete
    @Update("update task set is_deleted=true where task_id=#{taskId}")
    void delete(Integer taskId);

    void update(Task task);

    int findTaskCount(@Param("projectId") Integer projectId, @Param("search") String search);

    int findExistsTask(Integer taskId);
}
