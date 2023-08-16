package com.douzone.prosync.project.dto;

import com.douzone.prosync.project.entity.Project;
import lombok.*;


public class ProjectResponse {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class GetProjectResponse{
        private Integer projectId;
        private String name;
        private String intro;

        private String createdAt;
        private String startDate;
        private String endDate;
        private String modifiedAt;

        private Float progress;
        private Boolean publicyn;
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
        private Integer projectId;
        private String name;

        private String startDate;
        private String endDate;

        private Float progress;
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
        private Integer projectId;
    }

}
