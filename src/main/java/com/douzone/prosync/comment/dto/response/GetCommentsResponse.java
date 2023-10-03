package com.douzone.prosync.comment.dto.response;

import com.douzone.prosync.file.dto.FileResponseDto;
import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("[RESPONSE] COMMENT LIST - GET")
public class GetCommentsResponse {

    @ApiModelProperty(value = "댓글 식별자", example = "1")
    private Long commentId;

    @ApiModelProperty(value = "댓글 내용", example = "댓글 내용")
    private String content;

    @ApiModelProperty(value = "생성일자", example = "2023-10-10")
    private String createdAt;

    @ApiModelProperty(value = "수정일자", example = "2023-10-10")
    private String modifiedAt;

    @ApiModelProperty(value = "업무 식별자", example = "1")
    private Long taskId;

    // 멤버 아이디, 멤버 프로필 이미지, 멤버 이름
    @Setter
    private MemberProjectResponseDto memberInfo;

    @Setter
    private List<FileResponseDto> fileList = new ArrayList<>();

    @Setter
    private String files;

}