package com.douzone.prosync.member_project.dto;

import lombok.Getter;

@Getter
public class MemberProjectResponseDto {

    private Long memberProjectId;
    private Long memberId;
    private Long projectId;
    private Long authorityId;
    private String authority;
    private String username;
    private String profileImage;
}
