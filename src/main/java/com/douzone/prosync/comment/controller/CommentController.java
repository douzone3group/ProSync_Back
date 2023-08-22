package com.douzone.prosync.comment.controller;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.comment.dto.response.CommentSimpleResponse;
import com.douzone.prosync.comment.dto.response.GetCommentsResponse;
import com.douzone.prosync.comment.entity.Comment;
import com.douzone.prosync.comment.service.CommentServiceImpl;
import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.project.dto.response.ProjectSimpleResponse;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Validated
@Tag(name = "comment", description = "댓글 API")
@Slf4j
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
            @Parameter(description = "업무식별자", required = true, example = "1") @PathVariable("task-id") @Positive Long taskId,
            @RequestBody CommentPostDto dto,
            @Parameter(hidden = true) @ApiIgnore Principal principal) {

        dto.setMemberId(Long.valueOf(principal.getName()));
        dto.setTaskId(taskId);
        Integer commentId = commentService.save(dto);

        // 생성된 댓글의 ID를 반환

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
            @Parameter(description = "업무 식별자", required = true, example = "1") @PathVariable("task-id") Long taskId,
            @Parameter(description = "댓글 식별자", required = true, example = "1") @PathVariable("comment-id") Integer commentId,
            @RequestBody @Valid CommentPatchDto dto,
            @Parameter(hidden = true) @ApiIgnore Principal principal) {

        dto.setCommentId(commentId);

        log.info("dto.content={}", dto.getContent());
        commentService.update(dto);
        return new ResponseEntity(new CommentSimpleResponse(commentId), HttpStatus.OK);
    }


//     댓글 조회
    @GetMapping("/tasks/{task-id}/comments")
    @ApiOperation(value = "댓글 전체 조회",notes = "댓글을 전체 조회 한다",tags = "comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved",response = GetCommentsResponse.class),
            @ApiResponse(code = 404, message = "comment not found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "조회할 페이지 번호", defaultValue = "1", example = "1"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "한페이지에 보여질 요소 개수", defaultValue = "10", example = "20")})
    public ResponseEntity<PageResponseDto<GetCommentsResponse>> findAllComment(
            @Parameter(description = "업무 식별자",required = true,example = "1") @PathVariable("task-id") Long taskId,
            @Parameter(hidden = true) @ApiIgnore @PageableDefault (size=8, sort="commentId", direction = Sort.Direction.DESC) Pageable pageable){

        Page<Comment> pages = commentService.findCommentList(taskId,pageable);
        List<Comment> comments = pages.getContent();
        List<GetCommentsResponse> commentsResponses
                = comments.stream().map(GetCommentsResponse::of).collect(Collectors.toList());

        return new ResponseEntity(new PageResponseDto<>(commentsResponses,pages), HttpStatus.OK);
    }




    // 댓글 삭제
    @DeleteMapping("/tasks/{task-id}/comments/{comment-id}")
    @ApiOperation(value = "댓글 삭제", notes = "댓글을 소프트 삭제 한다", tags = "comment")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "successfully retrieved", response = CommentSimpleResponse.class),
            @ApiResponse(code = 404, message = "project not found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    public ResponseEntity deleteComment(
            @Parameter(description = "업무 식별자", required = true, example = "1") @PathVariable("task-id") Long taskId,
            @Parameter(description = "댓글 식별자", required = true, example = "1") @PathVariable("comment-id") Integer commentId,
            @ApiIgnore Principal principal) {
        commentService.delete(commentId);
        return new ResponseEntity(new CommentSimpleResponse(commentId), HttpStatus.NO_CONTENT);
    }


}
