package com.douzone.prosync.member_project.controller;

import com.douzone.prosync.common.SingleResponseDto;
import com.douzone.prosync.member_project.dto.MemberProjectRequestDto;
import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;
import com.douzone.prosync.member_project.service.MemberProjectService;
import com.douzone.prosync.project.dto.response.ProjectInviteDto;
import com.douzone.prosync.project.dto.response.ProjectSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberProjectController {

    private final MemberProjectService memberProjectService;

    /**
     * 프로젝트 초대 코드 생성 및 조회
     * ADMIN
     */
    @GetMapping("/projects/{project-id}/invitation")
    public ResponseEntity createInviteLink(@PathVariable("project-id") Integer projectId) {
        String inviteCode = memberProjectService.createInviteLink(projectId);
        return new ResponseEntity(new ProjectInviteDto(projectId, inviteCode), HttpStatus.OK);
    }

    /**
     * 프로젝트 회원 등록 [초대 링크 접속 -> reader 권한 부여]
     * LOGIN USER
     */
    @PostMapping("/invitation/{invite-code}")
    public ResponseEntity postProjectMember(@PathVariable("invite-code") String inviteCode,
                                            Principal principal) {
        Integer projectId = memberProjectService.createProjectMember(Long.parseLong(principal.getName()), inviteCode);
        return new ResponseEntity(new ProjectSimpleResponse(projectId), HttpStatus.OK);
    }

    /**
     * 프로젝트 멤버 삭제 [강퇴]
     * ADMIN
     */
    @DeleteMapping("/project-members/{project-member-id}")
    public ResponseEntity deleteProjectMember(@PathVariable("project-member-id") Long projectMemberId) {
        memberProjectService.deleteProjectMember(projectMemberId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 프로젝트 멤버 수정 [권한 변경]
     * ADMIN
     */
    @PatchMapping("/project-members/{project-member-id}")
    public ResponseEntity patchProjectMember(@PathVariable("project-member-id") Long projectMemberId,
                                             @RequestBody MemberProjectRequestDto dto,
                                             Principal principal) {
        memberProjectService.updateProjectMember(projectMemberId, dto, Long.parseLong(principal.getName()));
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 프로젝트 회원 조회
     * READER, WRITER, ADMIN
     */
    @GetMapping("/projects/{project-id}/members/{member-id}")
    public ResponseEntity getProjectMember(@PathVariable("project-id") Integer projectId,
                                           @PathVariable("member-id") Long memberId) {

        MemberProjectResponseDto response = memberProjectService.findProjectMember(projectId, memberId);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * 프로젝트 회원 목록 조회
     * READER, WRITER, ADMIN
     */
    @GetMapping("/projects/{project-id}/members")
    public ResponseEntity getProjectMembers(@PathVariable("project-id") Integer projectId) {
        List<MemberProjectResponseDto> projectMembers = memberProjectService.findProjectMembers(projectId);
        return new ResponseEntity(new SingleResponseDto<>(projectMembers), HttpStatus.OK);
    }

    /**
     * 자신의 프로젝트 나가기
     * READER, WRITER
     */
    @DeleteMapping("/projects/{project-id}/members")
    public ResponseEntity deleteProjectMemberBySelf(@PathVariable("project-id") Integer projectId,
                                 Principal principal) {
        memberProjectService.exitProjectMember(projectId, Long.parseLong(principal.getName()));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
