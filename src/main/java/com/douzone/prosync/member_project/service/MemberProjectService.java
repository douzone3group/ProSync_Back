package com.douzone.prosync.member_project.service;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.member_project.dto.MemberProjectRequestDto;
import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;
import com.douzone.prosync.member_project.dto.MemberProjectSearchCond;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberProjectService {

    String createInviteLink(Long projectId);

    String findInviteLink(Long projectId);

    Long createProjectMember(Long memberId, String inviteCode);

    void updateProjectMember(Long projectMemberIdt, MemberProjectRequestDto authority, Long memberId);

    MemberProjectResponseDto findProjectMember(Long projectId, Long memberId);

    PageResponseDto<MemberProjectResponseDto> findProjectMembers(MemberProjectSearchCond searchCond, Pageable pageable);

    void deleteProjectMember(Long projectMemberId, Long memberId);

    void exitProjectMember(Long projectId, Long memberId);

    List<Long> findProjectIdsByMemberId(Long memberId);
}