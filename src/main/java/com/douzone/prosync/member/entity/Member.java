package com.douzone.prosync.member.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member {

    // PK
    @ApiModelProperty(value = "멤버 pk", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @ApiModelProperty(value = "이메일", example = "abcd@naver.com")
    private String email;

    @ApiModelProperty(value = "비밀번호", example = "1234a")
    private String password;

    @ApiModelProperty(value = "닉네임", example = "hee")
    private String name;

    @ApiModelProperty(value = "생성일자", example = "2023-01-23")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "수정일자", example = "2023-12-31")
    private LocalDateTime modifiedAt;

    @ApiModelProperty(value = "탈퇴 여부", example = "false")
    private boolean isDeleted;

    @ApiModelProperty(value = "회원 소개글", example = "hi")
    private String intro;

    @ApiModelProperty(value = "닉네임(이메일)", example = "hee(abcd@naver.com)")
    private String nameEmail;



    // Todo: 파일 image 연결
    @ApiModelProperty(value = "프로필 이미지", example = "1")
    private String profileImage;
    }
