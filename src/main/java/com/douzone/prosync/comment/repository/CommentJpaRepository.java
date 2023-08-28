package com.douzone.prosync.comment.repository;

import com.douzone.prosync.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Integer> {


    Page<Comment> findAllByIsDeletedNullAndTaskId(Long taskId, Pageable pageable);


}
