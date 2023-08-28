package com.douzone.prosync.member_project.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberProject {

    private Long memberProjectId;
    private Long memberId;
    private Long projectId;
    private Long authorityId;
}
