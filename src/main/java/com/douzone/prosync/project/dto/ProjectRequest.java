package com.douzone.prosync.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ProjectRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PostDto {
        private String name;
        private String intro;

        private String startDate;
        private String endDate;

        private Boolean publicyn;
        private String projectImage;
    }

}
