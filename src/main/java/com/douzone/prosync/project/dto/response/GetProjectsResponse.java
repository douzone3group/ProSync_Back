package com.douzone.prosync.project.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel("GetProjectsResponse")
public class GetProjectsResponse {

    @ApiModelProperty(value = "프로젝트 아이디", example = "1")
    private Long projectId;

    @ApiModelProperty(value = "프로젝트 이름", example = "프로젝트 이름")
    private String title;

    @ApiModelProperty(value = "프로젝트 시작 날짜", example = "2023-10-16")
    private String startDate;

    @ApiModelProperty(value = "프로젝트 종료 날짜", example = "2023-10-16")
    private String endDate;

    @ApiModelProperty(value = "프로젝트 생성일", example = "2023-10-16 20:00:00")
    private String createdAt;

    @ApiModelProperty(value = "프로젝트 이미지", example = "http://~")
    private String projectImage;

    @ApiModelProperty(value = "프로젝트 admin 회원식별자", example = "1")
    private Long memberId;

    @ApiModelProperty(value = "프로젝트 admin 이름", example = "관리자 이름")
    private String name;

    @ApiModelProperty(value = "프로젝트 admin 프로필이미지", example = "http://~")
    private String profileImage;

    @ApiModelProperty(value = "북마크 아이디", example = "1")
    private Long bookmarkId;

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }
}