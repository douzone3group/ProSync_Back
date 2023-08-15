package com.douzone.prosync.task.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ApiModel("[RESPONSE] TASK - POST/PATCH/DELETE")
public class TaskSimpleResponse {

    @ApiModelProperty(value = "업무식별자", example = "1")
    private Integer taskId;

}
