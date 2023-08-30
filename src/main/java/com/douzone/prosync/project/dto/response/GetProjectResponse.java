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

    @ApiModelProperty(example = "프로젝트 아이디")
    private Long projectId;

    @ApiModelProperty(example = "프로젝트 이름")
    private String name;

    @ApiModelProperty(example = "프로젝트 인트로")
    private String intro;

    @ApiModelProperty(example = "프로젝트 생성 날짜")
    private String createdAt;

    @ApiModelProperty(example = "프로젝트 시작 날짜")
    private String startDate;

    @ApiModelProperty(example = "프로젝트 종료 날짜")
    private String endDate;

    @ApiModelProperty(example = "프로젝트 수정 날짜")
    private String modifiedAt;

    @ApiModelProperty(example = "프로젝트 공개여부")
    private Boolean isPublic;

    @ApiModelProperty(example = "프로젝트 이미지")
    private String projectImage;

    public static GetProjectResponse of(Project project) {
        return GetProjectResponse.builder()
                .projectId(project.getProjectId())
                .name(project.getTitle())
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
