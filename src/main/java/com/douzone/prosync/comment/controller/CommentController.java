package com.douzone.prosync.comment.controller;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.comment.dto.response.CommentSimpleResponse;
import com.douzone.prosync.comment.service.CommentServiceImpl;
import com.douzone.prosync.project.dto.response.ProjectSimpleResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Validated
@Tag(name = "comment", description = "댓글 API")
public class CommentController {

    private final CommentServiceImpl commentService;

    //댓글 생성
    @PostMapping("/tasks/{task-id}/comments")
    @Operation(summary = "댓글 생성", description = "업무에 대한 댓글을 생성합니다.", tags = "comment")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successfully retrieved", response = CommentSimpleResponse.class),
            @ApiResponse(code = 404, message = "not found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity<CommentSimpleResponse> PostComment(
            @Parameter(description = "업무식별자", required = true, example = "1") @PathVariable("task-id") @Positive Integer taskId,
            @RequestBody @Valid CommentPostDto dto,
            @Parameter(hidden = true) @ApiIgnore Principal principal) {

        Integer commentId = commentService.save(dto, taskId, Long.valueOf(principal.getName()));

        // 생성된 댓글의 ID를 반환
        return new ResponseEntity<>(new CommentSimpleResponse(commentId), HttpStatus.CREATED);
    }

    // 댓글 수정
    @PatchMapping("/tasks/{task-id}/comments/{comment-id}")
    @Operation(summary = "댓글 수정", description = "업무에 대한 댓글을 생성합니다.", tags = "comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = CommentSimpleResponse.class),
            @ApiResponse(code = 404, message = "not found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity updateComment(@Parameter(description = "업무 식별자", required = true, example = "1") @PathVariable("task-id") @Positive Integer taskId,
                                        @Parameter(description = "댓글 식별자", required = true, example = "1") @PathVariable("comment-id") @Positive Integer commentId,
                                        @RequestBody @Valid CommentPatchDto dto, @ApiIgnore Principal principal) {
        dto.setCommentId(commentId);
        commentService.update(dto,taskId,Long.valueOf(principal.getName()));
        return new ResponseEntity<>(new CommentSimpleResponse(commentId), HttpStatus.OK);
    }
//
//    // 댓글 조회
//    @GetMapping("/tasks/{task-id}/comments")
//    public ResponseEntity<PageResponseDto<GetCommentsResponse>> getCommentList(@Parameter(description = "업무 식별자", required = true, example = "1") @PathVariable("task-id") @Positive Integer taskId,
//                                                                               @Parameter(hidden = true) @ApiIgnore @PageableDefault(size = 10, sort = "commentId", direction = Sort.Direction.DESC) Pageable pageable) {
//
//    }
//
    // 댓글 삭제
    @DeleteMapping("/tasks/{task-id}/comments/{comment-id}")
    @ApiOperation(value = "댓글 삭제", notes = "댓글을 소프트 삭제 한다", tags = "comment")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "successfully retrieved", response = CommentSimpleResponse.class),
            @ApiResponse(code = 404, message = "project not found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    public ResponseEntity deleteComment(@Parameter(description = "업무 식별자", required = true, example = "1") @PathVariable("task-id") Integer taskId,
                                        @Parameter(description = "댓글 식별자", required = true, example = "1") @PathVariable("comment-id") Integer commentId,
                                        @ApiIgnore Principal principal) {
        commentService.delete(commentId,taskId,Long.valueOf(principal.getName()));
        return new ResponseEntity(new CommentSimpleResponse(commentId), HttpStatus.NO_CONTENT);
    }
}

