package com.douzone.prosync.project.dto.response;

import com.douzone.prosync.project.entity.Project;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@ApiModel("GetProjectResponse")
public class GetProjectResponse{

    @ApiModelProperty(value = "프로젝트 아이디", example = "1")
    private Long projectId;

    @ApiModelProperty(value = "프로젝트 이름", example = "프로젝트 이름")
    private String title;

    @ApiModelProperty(value = "프로젝트 인트로", example = "프로젝트를 소개합니다.")
    private String intro;

    @ApiModelProperty(value = "프로젝트 생성 날짜", example = "2023-10-09 23:06:57")
    private String createdAt;

    @ApiModelProperty(value = "프로젝트 시작 날짜", example = "2023-10-10")
    private String startDate;

    @ApiModelProperty(value = "프로젝트 종료 날짜", example = "2023-10-10")
    private String endDate;

    @ApiModelProperty(value = "프로젝트 수정 날짜", example = "2023-10-09 23:06:57")
    private String modifiedAt;

    @ApiModelProperty(value = "프로젝트 공개여부", example = "true/false")
    private Boolean isPublic;

    @ApiModelProperty(value = "프로젝트 이미지", example = "https://~~")
    private String projectImage;

    public static GetProjectResponse of(Project project) {
        return GetProjectResponse.builder()
                .projectId(project.getProjectId())
                .title(project.getTitle())
                .intro(project.getIntro())
                .createdAt(project.getCreatedAt().toString())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .modifiedAt(project.getModifiedAt().toString())
                .isPublic(project.getIsPublic())
                .projectImage(project.getProjectImage())
                .build();
    }
}
