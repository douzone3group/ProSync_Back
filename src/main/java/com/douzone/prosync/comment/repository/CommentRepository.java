package com.douzone.prosync.comment.repository;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.comment.entity.Comment;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.project.entity.Project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
@Slf4j
public class CommentRepository {

    private final CommentMybatisMapper commentMybatisMapper;

    // 댓글 생성
    public void createComment(CommentPostDto dto) {

        commentMybatisMapper.createComment(dto);

    }


    // 댓글 목록 조회
//    public List<Comment> findAllComment(CommentPostDto dto) {
//        return commentMybatisMapper.findAllComments();
//    }


    // 댓글 수정
    public void updateComment(CommentPatchDto dto) {
        commentMybatisMapper.updateComment(dto);
    }

    // 댓글 삭제
    public void deleteComment(Integer commentId) {
        commentMybatisMapper.deleteComment(commentId);
    }


    public Optional<Comment> checkMember(Integer commentId,Long memberId) {
        Optional<Comment> comment=commentMybatisMapper.checkMember(commentId,memberId);
        return comment;
    }
}
