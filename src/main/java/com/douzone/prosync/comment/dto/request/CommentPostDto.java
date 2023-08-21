package com.douzone.prosync.comment.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ApiModel("[REQUEST] COMMENT - POST")
public class CommentPostDto {

    @ApiModelProperty(value = "댓글 식별자", required = true, example = "댓글 식별자")
    private Integer commentId;

    @NotBlank
    @ApiModelProperty(value = "댓글 내용", required = true, example = "댓글 내용")
    private String content;

    @NotBlank
    @ApiModelProperty(value = "회원 식별자", required = true, example = "회원 식별자")
    private Integer memberId;

    @NotBlank
    @ApiModelProperty(value = "업무 식별자", required = true, example = "업무 식별자")
    private Integer taskId;

    @NotBlank
    @ApiModelProperty(value = "댓글 생성 일자", required = true, example = "댓글 생성 일자")
    private LocalDateTime createdAt;



}
