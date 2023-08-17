package com.douzone.prosync.member.entity;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    // PK
    private Long memberId;

    private String email;

    private String password;

    private String name;

    private Timestamp createdAt;

    private Timestamp modifiedAt;

    private boolean isDeleted;

    private String intro;



    // Todo: 파일 image 연결
    private String profileImage;
    }
