package com.douzone.prosync.member.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPatchPasswordDto {

    @ApiModelProperty(value = "비밀번호",example = "1234")
    @NotNull @NotBlank
    private String password;
}
