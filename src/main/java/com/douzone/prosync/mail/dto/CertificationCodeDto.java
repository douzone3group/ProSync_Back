package com.douzone.prosync.mail.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CertificationCodeDto {

    @Email
    private  String email;

    @NotNull @NotBlank
    private  String certificationNumber;
}
