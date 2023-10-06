package com.douzone.prosync.member.dto.request;

import com.douzone.prosync.file.basic.BasicImage;
import com.douzone.prosync.member.dto.MemberDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
        @Pattern(regexp = "^(?=.*[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\\\/-]).{8,15}$")
        private String password;


        @ApiModelProperty(example = "hong")
        @Pattern(regexp = "^[가-힣]{1,7}$")
        private String name;

        public MemberDto of(String modifiedPassword) {
               return MemberDto.builder()
                       .email(email)
                       .password(modifiedPassword)
                       .profileImage(BasicImage.BASIC_USER_IMAGE.getPath())
                       .name(name)
                       .modifiedAt(Timestamp.from(Instant.now()))
                       .createdAt(Timestamp.from(Instant.now()))
                       .intro("소개를 입력하세요.")
                       .isDeleted(false)
                       .build();
        }

}
