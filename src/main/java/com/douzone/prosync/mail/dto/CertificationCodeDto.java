package com.douzone.prosync.mail.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CertificationCodeDto {

    @ApiModelProperty(example = "이메일")
    @Email
    private  String email;

    @ApiModelProperty(example = "인증번호")
    @NotNull @NotBlank
    private  String certificationNumber;
}