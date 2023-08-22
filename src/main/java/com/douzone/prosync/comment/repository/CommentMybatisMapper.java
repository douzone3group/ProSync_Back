package com.douzone.prosync.comment.repository;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.project.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMybatisMapper {
    // 댓글 생성
    void createComment(CommentPostDto dto);

    // 댓글 목록 조회
    List<Project> findAllComments();

    // 댓글 수정
    void updateComment(CommentPatchDto dto);

    // 댓글 삭제
    void deleteComment(Integer commentId);
}
