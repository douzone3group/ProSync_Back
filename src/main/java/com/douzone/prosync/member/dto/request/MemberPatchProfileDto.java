package com.douzone.prosync.member.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPatchProfileDto {


        @ApiModelProperty(value = "닉네임" ,example = "hee")
        @Pattern(regexp = "^[가-힣]{1,7}$")
        private String name;

        @ApiModelProperty(value = "회원 소개글",example = "hi")
        @Size(min = 20, max = 500)
        private String intro;


        @ApiModelProperty(value = "파일식별자", example = "1")
        private Long fileId;

        @Setter
        private String profileImage;

}
