package com.douzone.prosync.task.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ApiModel("MemberSimpleResponse")
public class MemberSimpleResponseDto {

    @ApiModelProperty(value = "멤버 식별자", example = "1")
    private Long memberId;

}
