package com.douzone.prosync.comment.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@ApiModel("[RESPONSE] COMMENT LIST - GET")
public class GetCommentsResponse {
    @ApiModelProperty(value = "댓글 식별자", example = "1")
    private Integer commentId;

    @ApiModelProperty(value = "댓글 내용", example = "댓글 내용")
    private String content;

    @ApiModelProperty(value = "생성일자", example = "2023-10-10")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "수정일자", example = "2023-10-10")
    private LocalDateTime modifiedAt;

    @ApiModelProperty(value = "회원 식별자", example = "1")
    private Integer memberId;

    @ApiModelProperty(value = "업무 식별자", example = "1")
    private Integer taskId;

}
