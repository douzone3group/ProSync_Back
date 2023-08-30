package com.douzone.prosync.task.dto.request;

import com.douzone.prosync.member_project.entity.MemberProject;
import lombok.Getter;

@Getter
public class TaskMemberResponseDto {

    private Long memberProjectId;
    private MemberProject.MemberProjectStatus status;
    private Long memberId;
    private String profileImage;
    private String name;
}
