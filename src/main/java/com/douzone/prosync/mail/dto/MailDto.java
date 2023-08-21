package com.douzone.prosync.mail.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class MailDto {

    @Email
    private String email;
}
