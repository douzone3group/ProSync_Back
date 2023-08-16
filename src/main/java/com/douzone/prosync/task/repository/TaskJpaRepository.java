package com.douzone.prosync.task.repository;

import com.douzone.prosync.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskJpaRepository extends JpaRepository<Task, Integer> {

    @Query("SELECT t FROM Task t " +
            "WHERE (t.projectId = :projectId AND t.isDeleted IS NULL) " +
            "AND (t.title LIKE %:search% " +
            "OR t.taskStatus LIKE %:search%)"
    )
    Page<Task> findAllByProjectIdAndSearch(Integer projectId, String search, Pageable pageable);

    Page<Task> findByProjectIdAndIsDeletedNull(Integer projectId, Pageable pageable);
}
