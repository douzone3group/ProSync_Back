package com.douzone.prosync.comment.dto.response;

import com.douzone.prosync.comment.entity.Comment;
import com.douzone.prosync.member.dto.response.MemberGetResponse;
import com.douzone.prosync.member.entity.Member;
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
    private String createdAt;

    @ApiModelProperty(value = "수정일자", example = "2023-10-10")
    private String modifiedAt;

    @ApiModelProperty(value = "업무 식별자", example = "1")
    private Long taskId;

    // 멤버 아이디, 멤버 프로필 이미지, 멤버 이름
    private MemberGetResponse.SimpleResponse memberInfo;

    public static GetCommentsResponse of(Comment comment) {
        Member member = comment.getMember();
        return GetCommentsResponse.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt().toString())
                .modifiedAt(comment.getModifiedAt().toString())
                .taskId(comment.getTaskId())
                .memberInfo(new MemberGetResponse.SimpleResponse(member.getMemberId(), member.getProfileImage(), member.getName()))
                .build();
    }
}
