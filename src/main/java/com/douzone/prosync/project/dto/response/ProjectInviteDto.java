package com.douzone.prosync.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectInviteDto {

    private Integer projectId;
    private String inviteCode;
}
