package com.douzone.prosync.task.repository;

import com.douzone.prosync.task.entity.Task;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskMybatisMapper {

    void save(Task task);

    @Select("select * from task where task_id=#{taskId} and is_deleted is null")
    Optional<Task> findById(Integer taskId);


    List<Task> findTasksWithPagination(@Param("projectId") Integer projectId,
                                      @Param("offset") int offset,
                                      @Param("size") int size);

    @Delete("delete from task where task_id=#{taskId}")
    void deleteById(Integer taskId);

    //soft delete
    @Update("update task set is_deleted=true where task_id=#{taskId}")
    void delete(Integer taskId);

    void update(Task task);

    int getTaskCount(Integer projectId);

    int findExistsTask(Integer taskId);
}
