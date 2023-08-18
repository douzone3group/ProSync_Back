package com.douzone.prosync.member.dto.request;

import com.douzone.prosync.member.dto.MemberDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPostDto {

        @ApiModelProperty(example = "abcd@naver.com")
        @Email
        private String email;

        @ApiModelProperty(example = "1234")
        @NotBlank
        @NotNull
        private String password;

        public MemberDto of(String modifiedPassword) {
               return MemberDto.builder()
                       .email(email)
                       .password(modifiedPassword)
                       .profileImage("default") // TODO : 이미지 꼭 넣기
                       .name("")
                       .modifiedAt(Timestamp.from(Instant.now()))
                       .createdAt(Timestamp.from(Instant.now()))
                       .isDeleted(false)
                       .build();
        }

}
