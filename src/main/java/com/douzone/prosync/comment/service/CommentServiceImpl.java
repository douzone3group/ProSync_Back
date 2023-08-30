package com.douzone.prosync.comment.service;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.comment.dto.response.GetCommentsResponse;
import com.douzone.prosync.comment.entity.Comment;
import com.douzone.prosync.comment.repository.CommentRepository;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.service.TaskServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl  implements CommentService{

    private final TaskServiceImpl taskService;

    private final CommentRepository commentRepository;


    @Override
    public Integer save(CommentPostDto dto) {
        findExistTask(dto);

        commentRepository.createComment(dto);

        return dto.getCommentId();
    }



    @Override
    public void update(CommentPatchDto dto) {

        commentRepository.updateComment(dto);

    }

    @Override
    public void delete(Integer commentId) {
        commentRepository.deleteComment(commentId);
    }

    @Override
    public PageInfo<GetCommentsResponse> findCommentList(Long taskId, Pageable pageable) {
        // PageHelper 시작 - 페이지 및 페이지 크기 설정
        PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize()); // 페이지 인덱스는 1부터 시작합니다.

        // DB에서 데이터 가져오기
        List<GetCommentsResponse> commentsResponses = commentRepository.findAllComment(taskId);

        // PageInfo 객체 반환
        return new PageInfo<>(commentsResponses);
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

