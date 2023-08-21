package com.douzone.prosync.member.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPatchProfileDto {


        @ApiModelProperty(value = "닉네임" ,example = "hee")
        @NotNull @NotBlank
        private String name;

        @ApiModelProperty(value = "회원 소개글",example = "hi")
        @NotNull @NotBlank
        private String intro;


        // Todo: 파일 image 연결
        @ApiModelProperty(value = "프로필 이미지",example = "default")
        @NotNull @NotBlank
        private String profileImage;

}
