package com.douzone.prosync.member_project.service;

import com.douzone.prosync.member_project.dto.MemberProjectRequestDto;
import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;

import java.util.List;

public interface MemberProjectService {

    String createInviteLink(Integer projectId);

    Integer createProjectMember(Long memberId, String inviteCode);

    void updateProjectMember(Long projectMemberIdt, MemberProjectRequestDto authority, Long memberId);

    MemberProjectResponseDto findProjectMember(Integer projectId, Long memberId);

    List<MemberProjectResponseDto> findProjectMembers(Integer projectId);

    void deleteProjectMember(Long projectMemberId);

    void exitProjectMember(Integer projectId, Long memberId);
}
