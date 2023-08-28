package com.douzone.prosync.member_project.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class MemberProjectResponseDto {

    @ApiModelProperty(value = "프로젝트 회원 식별자", example = "1")
    private Long memberProjectId;

    @ApiModelProperty(value = "회원 식별자", example = "1")
    private Long memberId;

    @ApiModelProperty(value = "프로젝트 식별자", example = "1")
    private Long projectId;

    @ApiModelProperty(value = "권한 식별자", example = "1")
    private Long authorityId;

    @ApiModelProperty(value = "권한", example = "1")
    private String authority;

    @ApiModelProperty(value = "회원 이름", example = "1")
    private String username;

    @ApiModelProperty(value = "회원 프로필", example = "1")
    private String profileImage;
}
