package com.douzone.prosync.comment.service;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.comment.entity.Comment;
import com.douzone.prosync.comment.repository.CommentJpaRepository;
import com.douzone.prosync.comment.repository.CommentRepository;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.service.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl  implements CommentService{

    private final TaskServiceImpl taskService;

    private final CommentRepository commentRepository;

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Integer save(CommentPostDto dto) {
        findExistTask(dto);
        dto.setCreatedAt(LocalDateTime.now());

        commentRepository.createComment(dto);

        return dto.getCommentId();
    }


    @Override
    public void update(CommentPatchDto dto) {
        dto.setModifiedAt(LocalDateTime.now());

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

    @Override
    public Boolean checkMember(Integer commentId, Long memberId) {

        Optional<Comment> comment = commentRepository.checkMember(commentId,memberId);

        return comment.isPresent() ? true : false;

    }
    private void findExistTask(CommentPostDto dto) {
        GetTaskResponse findTask = taskService.findTask(dto.getTaskId(), dto.getMemberId());
        if (findTask == null) {
            throw new ApplicationException(ErrorCode.TASK_NOT_FOUND);
        }
    }


}


