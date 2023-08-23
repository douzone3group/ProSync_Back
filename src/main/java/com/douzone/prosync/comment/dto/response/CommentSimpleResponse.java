package com.douzone.prosync.comment.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ApiModel("CommentSimpleResponse")
public class CommentSimpleResponse {

    @ApiModelProperty(value = "댓글식별자", example = "1")
    private Integer commentId;
}
