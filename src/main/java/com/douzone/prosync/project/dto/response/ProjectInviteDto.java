package com.douzone.prosync.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProjectInviteDto  {
    private Integer projectId;
    private String inviteCode;
}
