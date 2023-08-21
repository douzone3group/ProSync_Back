package com.douzone.prosync.comment.service;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.comment.entity.Comment;
import com.douzone.prosync.comment.repository.CommentMybatisMapper;
import com.douzone.prosync.comment.repository.CommentRepository;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.entity.Task;
import com.douzone.prosync.task.service.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl  implements CommentService{

    private final TaskServiceImpl taskService;

    private final CommentRepository commentRepository;

    @Override
    public Integer save(CommentPostDto dto, Integer taskId,Long memberId) {
        dto.setCreatedAt(LocalDateTime.now());

        GetTaskResponse findTask = taskService.findTask(taskId, null);

        commentRepository.createComment(dto, findTask.getTaskId());

        return dto.getCommentId();
    }

    @Override
    public void update(CommentPatchDto dto, Integer commentId, Long memberId) {
        dto.setModifiedAt(LocalDateTime.now());

//        commentRepository.updateComment(dto, findTask.getTaskId());


    }

    @Override
    public void delete(Integer commentId, Integer taskId,Long memberId) {
        commentRepository.deleteComment(commentId,taskId, memberId);
    }

//    @Override
//    public Page<Comment> findCommentList(Pageable pageable) {
//        return null;
//    }


}
