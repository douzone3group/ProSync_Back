package com.douzone.prosync.member_project.dto;

import com.douzone.prosync.member_project.status.ProjectMemberAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberProjectRequestDto {

    private ProjectMemberAuthority authority;
}
