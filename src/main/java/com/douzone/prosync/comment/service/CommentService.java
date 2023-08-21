package com.douzone.prosync.comment.service;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.comment.entity.Comment;
import com.douzone.prosync.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


public interface CommentService {

    // 댓글 생성
    Integer save(CommentPostDto dto,  Integer taskId,Long memberId);

    // 댓글 수정
    void update(CommentPatchDto dto, Integer commentId, Long taskId);

    // 댓글 삭제 (소프트)
    void delete(Integer CommentId, Integer taskId, Long memberId);

//    // 댓글 리스트 조회
//    Page<Comment> findCommentList(Pageable pageable);


}
