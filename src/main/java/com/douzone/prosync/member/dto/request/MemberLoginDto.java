package com.douzone.prosync.member.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MemberLoginDto {


        @ApiModelProperty(value = "이메일",example = "hdh960326@gmail.com")
        @Email
        private String email;

        @ApiModelProperty(value = "비밀번호", example = "aaaaa")
        private String password;


}
