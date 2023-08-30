package com.douzone.prosync.comment.service;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.comment.entity.Comment;
import com.douzone.prosync.common.PageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CommentService {

    // 댓글 생성
    Long save(CommentPostDto dto);

    // 댓글 수정
    void update(CommentPatchDto dto, Long memberId);

    // 댓글 삭제 (소프트)
    void delete(Long commentId, Long memberId);

    // 댓글 리스트 조회
    PageResponseDto findCommentList(Long taskId, Pageable pageable);
}
