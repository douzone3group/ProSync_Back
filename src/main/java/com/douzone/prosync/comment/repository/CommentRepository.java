package com.douzone.prosync.comment.repository;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.comment.dto.response.GetCommentsResponse;
import com.douzone.prosync.comment.entity.Comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
@Slf4j
public class CommentRepository {

    private final CommentMapper commentMapper;

    // 댓글 생성
    public void createComment(CommentPostDto dto) {

        commentMapper.createComment(dto);

    }


//     댓글 목록 조회
    public List<GetCommentsResponse> findAllComment(Long taskId) {
        return commentMapper.findAllComments(taskId);
    }


    // 댓글 수정
    public void updateComment(CommentPatchDto dto) {
        commentMapper.updateComment(dto);
    }

    // 댓글 삭제
    public void deleteComment(Integer commentId) {
        commentMapper.deleteComment(commentId);
    }


    public Optional<Comment> checkMember(Integer commentId,Long memberId) {
        Optional<Comment> comment= commentMapper.checkMember(commentId,memberId);
        return comment;
    }
}
