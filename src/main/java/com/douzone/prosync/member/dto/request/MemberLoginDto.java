package com.douzone.prosync.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MemberLoginDto {



        @Email
        private String email;

        @NotBlank
        @NotNull
        private String password;


}
