package com.douzone.prosync.project.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProjectInviteDto  {

    @ApiModelProperty(value = "프로젝트식별자", example = "1")
    private Long projectId;

    @ApiModelProperty(value = "초대코드", example = "83029ab18150488998bfdf07e4ec4d42")
    private String inviteCode;
}
