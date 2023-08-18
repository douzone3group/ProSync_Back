package com.douzone.prosync.project.dto;

import com.douzone.prosync.project.entity.Project;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


public class ProjectResponse {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class GetProjectResponse{

        @ApiModelProperty(example = "프로젝트 아이디")
        private Integer projectId;

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

        @ApiModelProperty(example = "프로젝트 진행도")
        private Float progress;

        @ApiModelProperty(example = "프로젝트 공개여부")
        private Boolean publicyn;

        @ApiModelProperty(example = "프로젝트 이미지")
        private String projectImage;

        public static GetProjectResponse of(Project project) {
            return GetProjectResponse.builder()
                    .projectId(project.getProjectId())
                    .name(project.getName())
                    .intro(project.getIntro())
                    .createdAt(project.getCreatedAt().toString())
                    .startDate(project.getStartDate())
                    .endDate(project.getEndDate())
                    .modifiedAt(project.getModifiedAt().toString())
                    .progress(project.getProgress())
                    .publicyn(project.getPublicyn())
                    .projectImage(project.getProjectImage())
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class GetProjectsResponse{

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

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SimpleResponse {

        @ApiModelProperty(example = "프로젝트 아이디")
        private Integer projectId;
    }

}
