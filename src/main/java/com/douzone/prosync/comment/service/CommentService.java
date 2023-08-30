package com.douzone.prosync.comment.service;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.comment.dto.response.GetCommentsResponse;
import com.douzone.prosync.comment.entity.Comment;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CommentService {

    // 댓글 생성
    Integer save(CommentPostDto dto);

    // 댓글 수정
    void update(CommentPatchDto dto);

    // 댓글 삭제 (소프트)
    void delete(Integer CommentId);

    // 댓글 리스트 조회
    PageInfo<GetCommentsResponse> findCommentList(Long taskId, Pageable pageable);


    Boolean checkMember(Integer commentId, Long memberId);
}
