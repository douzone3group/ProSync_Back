package com.douzone.prosync.comment.controller;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.comment.dto.response.CommentSimpleResponse;
import com.douzone.prosync.comment.dto.response.GetCommentsResponse;
import com.douzone.prosync.comment.service.CommentService;
import com.douzone.prosync.common.PageResponseDto;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    //댓글 생성
    @PostMapping("/tasks/{task-id}/comments")
    @Operation(summary = "댓글 생성", description = "업무에 대한 댓글을 생성합니다.", tags = "comment")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successfully retrieved", response = CommentSimpleResponse.class),
            @ApiResponse(code = 404, message = "not found"),
            @ApiResponse(code = 500, message = "Internal server error"),
    })
    public ResponseEntity<CommentSimpleResponse> postComment(
            @Parameter(description = "업무식별자", required = true, example = "1") @PathVariable("task-id") @Positive Long taskId,
            @RequestBody CommentPostDto dto,
            @Parameter(hidden = true) @ApiIgnore Principal principal) {

        // 업무 존재 여부 확인
        dto.setMemberId(Long.valueOf(principal.getName()));
        dto.setTaskId(taskId);
        Long commentId = commentService.save(dto);

        return new ResponseEntity<>(new CommentSimpleResponse(commentId), HttpStatus.CREATED);
    }

    //     댓글 수정
    @PatchMapping("/tasks/{task-id}/comments/{comment-id}")
    @Operation(summary = "댓글 수정", description = "업무에 대한 댓글을 생성합니다.", tags = "comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = CommentSimpleResponse.class),
            @ApiResponse(code = 404, message = "not found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity updateComment(
            @Parameter(description = "댓글 식별자", required = true, example = "1") @PathVariable("comment-id") Long commentId,
            @RequestBody @Valid CommentPatchDto dto,
            @Parameter(hidden = true) @ApiIgnore Principal principal) {

        dto.setCommentId(commentId);
        commentService.update(dto, Long.parseLong(principal.getName()));
        return new ResponseEntity(new CommentSimpleResponse(commentId), HttpStatus.OK);
    }


    //     댓글 조회
    @GetMapping("/tasks/{task-id}/comments")
    @ApiOperation(value = "댓글 전체 조회", notes = "댓글을 전체 조회 한다", tags = "comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = GetCommentsResponse.class),
            @ApiResponse(code = 404, message = "comment not found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "조회할 페이지 번호", defaultValue = "1", example = "1"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "한페이지에 보여질 요소 개수", defaultValue = "10", example = "20")})
    public ResponseEntity<PageResponseDto<GetCommentsResponse>> findAllComment(
            @Parameter(description = "업무 식별자", required = true, example = "1") @PathVariable("task-id") Long taskId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {

        PageResponseDto<GetCommentsResponse> response = commentService.findCommentList(taskId, page, size);
        return new ResponseEntity(response, HttpStatus.OK);
    }




    // 댓글 삭제
    @DeleteMapping("/tasks/{task-id}/comments/{comment-id}")
    @ApiOperation(value = "댓글 삭제", notes = "댓글을 소프트 삭제 한다", tags = "comment")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "successfully retrieved", response = CommentSimpleResponse.class),
            @ApiResponse(code = 404, message = "comment not found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    public ResponseEntity deleteComment(
            @Parameter(description = "댓글 식별자", required = true, example = "1") @PathVariable("comment-id") Long commentId,
            @ApiIgnore Principal principal) {

        commentService.delete(commentId, Long.parseLong(principal.getName()));
        return new ResponseEntity(new CommentSimpleResponse(commentId), HttpStatus.NO_CONTENT);
    }

}