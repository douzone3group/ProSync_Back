package com.douzone.prosync.member.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileWithAuthorityDto {

    @ApiModelProperty(value = "생성일자", example = "2023-01-23")
    private String createdAt;


    @ApiModelProperty(value = "회원 소개글", example = "hi")
    private String intro;

    @ApiModelProperty(value = "프로필 이미지", example = "default")
    private String profileImage;

    @ApiModelProperty(value = "닉네임(이메일)", example = "hee(abcd@naver.com)")
    private String nameEmail;

    @ApiModelProperty(value="프로젝트 권한", example = "reader")
    private String authority;
}
