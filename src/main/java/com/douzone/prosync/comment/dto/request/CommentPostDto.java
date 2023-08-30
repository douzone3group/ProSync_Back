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
    private Long commentId;

    @NotBlank
    @ApiModelProperty(value = "댓글 내용", required = true, example = "댓글 내용")
    private String content;

    private Long memberId;

    private Long taskId;

    private Long memberProjectId;
}