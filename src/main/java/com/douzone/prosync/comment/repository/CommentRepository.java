package com.douzone.prosync.comment.repository;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.project.entity.Project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final CommentMybatisMapper commentMybatisMapper;

    // 댓글 생성
    public void createComment(CommentPostDto dto,Integer taskId) {

        commentMybatisMapper.createComment(dto,taskId);

    }

    // 프로젝트 목록 조회
    public List<Project> findAllComment(CommentPostDto dto) {
        return commentMybatisMapper.findAllComments();
    }


    // 프로젝트 수정
    public void updateComment(CommentPatchDto dto, Integer taskId) {
        commentMybatisMapper.updateComment(dto,taskId);

    }

    // 프로젝트 삭제
    public void deleteComment(Integer projectId,Integer taskId,Long memberId) {
        commentMybatisMapper.deleteComment(projectId,taskId,memberId);
    }
}
