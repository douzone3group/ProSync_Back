package com.douzone.prosync.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class ProjectRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PostDto {
        private Integer projectId;
        private String name;
        private String intro;

        private String startDate;
        private String endDate;


        private Boolean publicyn;
        private String projectImage;

        private LocalDateTime createdAt;
    }



    @Getter
    @Setter
    @NoArgsConstructor
    public static class PatchDto {

        private Integer projectId;
        private String name;
        private String intro;

        private String startDate;
        private String endDate;

        private Boolean publicyn;
        private String projectImage;

        private LocalDateTime modifiedAt;
    }


}
