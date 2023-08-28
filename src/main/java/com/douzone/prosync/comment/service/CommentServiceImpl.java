package com.douzone.prosync.comment.service;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.comment.entity.Comment;
import com.douzone.prosync.comment.repository.CommentJpaRepository;
import com.douzone.prosync.comment.repository.CommentMybatisMapper;
import com.douzone.prosync.comment.repository.CommentRepository;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.entity.Task;
import com.douzone.prosync.task.service.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl  implements CommentService{

    private final TaskServiceImpl taskService;

    private final CommentRepository commentRepository;

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Integer save(CommentPostDto dto) {
        dto.setCreatedAt(LocalDateTime.now());

//        GetTaskResponse findTask = taskService.findTask(dto.getTaskId(), dto.getMemberId());

        commentRepository.createComment(dto);

        return dto.getCommentId();
    }

    @Override
    public void update(CommentPatchDto dto) {
        dto.setModifiedAt(LocalDateTime.now());
        log.info("dto.content={}", dto.getContent());

//        GetTaskResponse findTask = taskService.findTask(dto.getTaskId(), dto.getMemberId());

        commentRepository.updateComment(dto);

    }

    @Override
    public void delete(Integer commentId) {
        commentRepository.deleteComment(commentId);
    }

    @Override
    public Page<Comment> findCommentList(Long taskId,Pageable pageable) {
        return commentJpaRepository.findAllByIsDeletedNullAndTaskId(taskId,pageable);
    }


}
