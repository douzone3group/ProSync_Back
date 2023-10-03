package com.douzone.prosync.mail.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class MailDto {

    @ApiModelProperty(example = "이메일")
    @Email
    private String email;
}
