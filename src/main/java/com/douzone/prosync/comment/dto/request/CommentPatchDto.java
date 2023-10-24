package com.douzone.prosync.comment.dto.request;

import com.douzone.prosync.validator.HtmlTagExcluded;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("[REQUEST] COMMENT - PATCH")
public class CommentPatchDto {

    @ApiModelProperty(value = "댓글 식별자", required = true, example = "댓글 식별자")
    private Long commentId;

    @NotBlank
    @ApiModelProperty(value = "댓글 내용", required = true, example = "댓글 내용")
    @HtmlTagExcluded(maxLength = 300, message = "댓글은 1 ~ 300자 이내여야 합니다.")
    private String content;

    @ApiModelProperty(value = "파일식별자", example = "[1, 2, 3]")
    private List<Long> fileIds = new ArrayList<>();

}