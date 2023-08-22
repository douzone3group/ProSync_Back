package com.douzone.prosync.comment.repository;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.project.entity.Project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
@Slf4j
public class CommentRepository {

    private final CommentMybatisMapper commentMybatisMapper;

    // 댓글 생성
    public void createComment(CommentPostDto dto) {

        commentMybatisMapper.createComment(dto);

    }

    // 프로젝트 목록 조회
    public List<Project> findAllComment(CommentPostDto dto) {
        return commentMybatisMapper.findAllComments();
    }


    // 프로젝트 수정
    public void updateComment(CommentPatchDto dto) {

        commentMybatisMapper.updateComment(dto);

        log.info("dto.content={}", dto.getContent());


    }

    // 프로젝트 삭제
    public void deleteComment(Integer commentId) {
        commentMybatisMapper.deleteComment(commentId);
    }
}
