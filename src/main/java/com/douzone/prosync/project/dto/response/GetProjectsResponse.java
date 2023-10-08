package com.douzone.prosync.project.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel("GetProjectsResponse")
public class GetProjectsResponse {

    @ApiModelProperty(example = "프로젝트 아이디")
    private Long projectId;

    @ApiModelProperty(example = "프로젝트 이름")
    private String title;

    @ApiModelProperty(example = "프로젝트 시작 날짜")
    private String startDate;

    @ApiModelProperty(example = "프로젝트 종료 날짜")
    private String endDate;

    @ApiModelProperty(example = "프로젝트 생성일")
    private String createdAt;

    @ApiModelProperty(example = "프로젝트 이미지")
    private String projectImage;

    @ApiModelProperty(example = "프로젝트 admin 회원식별자")
    private Long memberId;

    @ApiModelProperty(example = "프로젝트 admin 이름")
    private String name;

    @ApiModelProperty(example = "프로젝트 admin 프로필이미지")
    private String profileImage;

    @ApiModelProperty(example = "북마크 아이디")
    private Long bookmarkId;

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }
}