package com.douzone.prosync.project.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class ProjectRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PostDto {

        @ApiModelProperty(example = "프로젝트 아이디")
        private Integer projectId;

        @ApiModelProperty(example = "프로젝트 이름")
        private String name;

        @ApiModelProperty(example = "프로젝트 인트로")
        private String intro;

        @ApiModelProperty(example = "프로젝트 시작 날짜")
        private String startDate;

        @ApiModelProperty(example = "프로젝트 종료 날짜")
        private String endDate;

        @ApiModelProperty(example = "프로젝트 공개여부")
        private Boolean publicyn;

        @ApiModelProperty(example = "프로젝트 이미지")
        private String projectImage;

        @ApiModelProperty(example = "프로젝트 생성 날짜")
        private LocalDateTime createdAt;
    }



    @Getter
    @Setter
    @NoArgsConstructor
    public static class PatchDto {

        @ApiModelProperty(example = "프로젝트 아이디")
        private Integer projectId;

        @ApiModelProperty(example = "프로젝트 이름")
        private String name;

        @ApiModelProperty(example = "프로젝트 인트로")
        private String intro;

        @ApiModelProperty(example = "프로젝트 시작 날짜")
        private String startDate;

        @ApiModelProperty(example = "프로젝트 종료 날짜")
        private String endDate;

        @ApiModelProperty(example = "프로젝트 공개여부")
        private Boolean publicyn;

        @ApiModelProperty(example = "프로젝트 이미지")
        private String projectImage;


        @ApiModelProperty(example = "프로젝트 수정 날짜")
        private LocalDateTime modifiedAt;
    }


}
