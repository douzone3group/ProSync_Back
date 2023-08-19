package com.douzone.prosync.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

    // PK
    @ApiModelProperty(example = "member pk")
    private Long memberId;

    @ApiModelProperty(example = "이메일")
    private String email;

    @ApiModelProperty(example = "비밀번호")
    private String password;

    @ApiModelProperty(example = "닉네임")
    private String name;

    @ApiModelProperty(example = "생성일자")
    private Timestamp createdAt;

    @ApiModelProperty(example = "수정일자")
    private Timestamp modifiedAt;

    @ApiModelProperty(example = "탈퇴여부")
    private boolean isDeleted;

    @ApiModelProperty(example = "멤버 소개글")
    private String intro;



    // Todo: 파일 image 연결
    @ApiModelProperty(example = "프로필 이미지")
    private String profileImage;
}