package com.douzone.prosync.member_project.service;

import com.douzone.prosync.member_project.dto.MemberProjectRequestDto;
import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;

import java.util.List;

public interface MemberProjectService {

    String createInviteLink(Long projectId);

    String findInviteLink(Long projectId);

    Long createProjectMember(Long memberId, String inviteCode);

    void updateProjectMember(Long projectMemberIdt, MemberProjectRequestDto authority, Long memberId);

    MemberProjectResponseDto findProjectMember(Long projectId, Long memberId);

    List<MemberProjectResponseDto> findProjectMembers(Long projectId);

    void deleteProjectMember(Long projectMemberId, Long memberId);

    void exitProjectMember(Long projectId, Long memberId);

    List<Long> findProjectIdsByMemberId(Long memberId);
}