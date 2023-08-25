package com.douzone.prosync.project.dto.response;

import com.douzone.prosync.member.dto.request.MemberSimpleDto;
import com.douzone.prosync.project.entity.Project;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@ApiModel("GetProjectsResponse")
public class GetProjectsResponse{

    @ApiModelProperty(example = "프로젝트 아이디")
    private Integer projectId;

    @ApiModelProperty(example = "프로젝트 이름")
    private String name;

    @ApiModelProperty(example = "프로젝트 시작 날짜")
    private String startDate;

    @ApiModelProperty(example = "프로젝트 종료 날짜")
    private String endDate;

    @ApiModelProperty(example = "프로젝트 진행도")
    private Float progress;

    @ApiModelProperty(example = "프로젝트 이미지")
    private String projectImage;
    // TODO: 회원 정보 추가

    public static GetProjectsResponse of(Project project) {
        return GetProjectsResponse.builder()
                .projectId(project.getProjectId())
                .name(project.getName())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .progress(project.getProgress())
                .projectImage(project.getProjectImage())
                .build();
    }

}