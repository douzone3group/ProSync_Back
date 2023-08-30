package com.douzone.prosync.comment.repository;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.comment.dto.response.GetCommentsResponse;
import com.douzone.prosync.comment.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentMapper {
    // 댓글 생성
    void createComment(CommentPostDto dto);

    // 댓글 목록 조회
    List<GetCommentsResponse> findAllComments(Long taskId);

    // 댓글 수정
    void updateComment(CommentPatchDto dto);

    // 댓글 삭제
    void deleteComment(Integer commentId);

//     댓글 멤버 검증
    Optional<Comment> checkMember(Integer commentId,Long memberId);
}
