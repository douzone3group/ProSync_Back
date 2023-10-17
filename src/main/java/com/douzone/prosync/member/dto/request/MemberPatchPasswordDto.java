package com.douzone.prosync.member.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPatchPasswordDto {

    @ApiModelProperty(value = "비밀번호",example = "1234")
    @Pattern(regexp = "^(?=.*[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\\\/-]).{8,15}$")
    private String password;
}
