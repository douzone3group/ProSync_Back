package com.douzone.prosync.task.repository;

import com.douzone.prosync.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskJpaRepository extends JpaRepository<Task, Integer> {

    @Query("SELECT t FROM Task t " +
            "WHERE (t.project.projectId = :projectId AND t.isDeleted IS NULL) " +
            "AND (t.title LIKE %:search% " +
            "OR t.taskStatus.taskStatus LIKE %:search%)"
            //TODO: 검색 조건 추가 (담당자)
    )
    Page<Task> findAllByProjectIdAndSearch(Integer projectId, String search, Pageable pageable);

    @Query("SELECT t FROM Task t " +
            "WHERE (t.project.projectId = :projectId AND t.isDeleted IS NULL)"
    )
    Page<Task> findByProjectIdAndIsDeletedNull(Integer projectId, Pageable pageable);
}
