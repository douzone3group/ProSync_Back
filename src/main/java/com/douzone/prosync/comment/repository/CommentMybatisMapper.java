package com.douzone.prosync.comment.repository;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.comment.dto.response.GetCommentsResponse;
import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentMybatisMapper {
    // 댓글 생성
    void createComment(CommentPostDto dto);

    // 댓글 목록 조회
    List<GetCommentsResponse> findAllComments(Long taskId);

    // 댓글 수정
    void updateComment(CommentPatchDto dto);

    // 댓글 삭제
    void deleteComment(Long commentId);

    @Select("select project_id from task where task_id = #{taskId} and is_deleted is null")
    Long findProjectIdByTask(Long taskId);

    Optional<MemberProjectResponseDto> findCommentMember(Long commentId);
}