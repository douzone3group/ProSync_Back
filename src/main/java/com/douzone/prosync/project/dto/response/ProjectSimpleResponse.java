package com.douzone.prosync.project.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel("ProjectSimpleResponse")
public class ProjectSimpleResponse {

    @ApiModelProperty(value = "프로젝트 식별자", example = "1")
    private Integer projectId;
}