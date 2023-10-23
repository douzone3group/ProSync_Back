package com.douzone.prosync.project.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@ApiModel("[REQUEST] PROJECT - PATCH")
public  class ProjectPatchDto {

    @ApiModelProperty(value = "프로젝트 식별자", required = true, example = "프로젝트 식별자")
    private Long projectId;

    @NotBlank
    @ApiModelProperty(value = "프로젝트 이름", required = true, example = "프로젝트 이름")
    @Length(max = 50,message = "프로젝트 이름은 50자 이내여야 합니다")
    private String title;

    @NotNull
    @ApiModelProperty(value = "프로젝트 소개", required = true, example = "프로젝트 소개")
    @HtmlTagExcluded(message = "프로젝트 소개는 1 ~ 1000자 이내여야 합니다.")
    private String intro;

    @NotBlank
    @ApiModelProperty(value = "프로젝트 시작 날짜", required = true, example = "프로젝트 시작 날짜")
    private String startDate;

    @NotBlank
    @ApiModelProperty(value = "프로젝트 종료 날짜", required = true, example = "프로젝트 종료 날짜")
    private String endDate;

    @NotNull
    @ApiModelProperty(value = "프로젝트 공개여부", required = true, example = "프로젝트 공개여부")
    private Boolean isPublic;

    @ApiModelProperty(value = "파일식별자", example = "1")
    private Long fileId;

    private String projectImage;

    public ProjectPatchDto(Long projectId, Long fileId, String projectImage) {
        this.projectId = projectId;
        this.fileId = fileId;
        this.projectImage = projectImage;
    }
}
