package com.douzone.prosync.task.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;

@Getter
@ApiModel("[REQUEST] MEMBER_TASK - POST")
public class TaskMemberRequestDto {


    @ApiModelProperty(value = "프로젝트회원식별자", example = "[ 1, 2, 3 ]")
    private List<Long> projectMemberIds;
}