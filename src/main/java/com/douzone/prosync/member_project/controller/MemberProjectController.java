package com.douzone.prosync.member_project.controller;

import com.douzone.prosync.common.SingleResponseDto;
import com.douzone.prosync.member_project.dto.MemberProjectRequestDto;
import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;
import com.douzone.prosync.member_project.service.MemberProjectService;
import com.douzone.prosync.project.dto.response.ProjectInviteDto;
import com.douzone.prosync.project.dto.response.ProjectSimpleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
@Tag(name = "member_project", description = "프로젝트 회원 API")
public class MemberProjectController {

    private final MemberProjectService memberProjectService;

    /**
     * 프로젝트 초대 코드 생성
     * ADMIN
     */
    @PostMapping("/projects/{project-id}/invitation")
    @Operation(summary = "초대 링크 조회", description = "프로젝트 회원으로 등록을 위한 초대 링크를 생합니다.", tags = "member_project")
    public ResponseEntity<SingleResponseDto<ProjectInviteDto>> createInviteLink(@Parameter(description = "프로젝트식별자", required = true, example = "1") @PathVariable("project-id") Long projectId) {
        String inviteCode = memberProjectService.createInviteLink(projectId);
        return new ResponseEntity(new SingleResponseDto<>(new ProjectInviteDto(projectId, inviteCode)), HttpStatus.OK);
    }

    /**
     * 프로젝트 초대 코드 조회
     */
    @GetMapping("/projects/{project-id}/invitation")
    @Operation(summary = "초대 링크 조회", description = "프로젝트 회원으로 등록을 위한 초대 링크를 생성합니다.", tags = "member_project")
    public ResponseEntity<SingleResponseDto<ProjectInviteDto>> getInviteLink(@Parameter(description = "프로젝트식별자", required = true, example = "1") @PathVariable("project-id") Long projectId) {
        String inviteCode = memberProjectService.findInviteLink(projectId);
        return new ResponseEntity(new SingleResponseDto<>(new ProjectInviteDto(projectId, inviteCode)), HttpStatus.OK);
    }

    /**
     * 프로젝트 회원 등록 [초대 링크 접속 -> reader 권한 부여]
     * LOGIN USER
     */
    @PostMapping("/invitation/{invite-code}")
    @Operation(summary = "프로젝트 회원 등록", description = "초대 코드를 확인하여 해당 프로젝트 회원으로 등록합니다 (기본권한 : READER)", tags = "member_project")
    public ResponseEntity<SingleResponseDto<ProjectSimpleResponse>> postProjectMember(@Parameter(description = "초대코드", required = true, example = "83029ab18150488998bfdf07e4ec4d42") @PathVariable("invite-code") String inviteCode,
                                            @Parameter(hidden = true) @ApiIgnore Principal principal) {
        Long projectId = memberProjectService.createProjectMember(Long.parseLong(principal.getName()), inviteCode);
        return new ResponseEntity(new SingleResponseDto<>(new ProjectSimpleResponse(projectId)), HttpStatus.OK);
    }

    /**
     * 프로젝트 멤버 삭제 [강퇴]
     * ADMIN
     */
    @DeleteMapping("/project-members/{project-member-id}")
    @Operation(summary = "프로젝트 회원 삭제", description = "프로젝트 관리자가 프로젝트의 회원을 삭제(강퇴)합니다.", tags = "member_project")
    public ResponseEntity deleteProjectMember(@Parameter(description = "프로젝트회원식별자", required = true, example = "1")@PathVariable("project-member-id") Long projectMemberId,
                                              @Parameter(hidden = true) @ApiIgnore Principal principal) {
        memberProjectService.deleteProjectMember(projectMemberId, getMemberId(principal));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 프로젝트 멤버 수정 [권한 변경]
     * ADMIN
     */
    @PatchMapping("/project-members/{project-member-id}")
    @Operation(summary = "프로젝트 회원 수정 (권한 변경)", description = "프로젝트 관리자가 프로젝트 회원의 권한을 수정합니다. 회원의 권한을 admin으로 수정할 경우 기존 관리자는 WRITER가 됩니다.", tags = "member_project")
    public ResponseEntity patchProjectMember(@Parameter(description = "프로젝트회원식별자", required = true, example = "1")@PathVariable("project-member-id") Long projectMemberId,
                                             @RequestBody MemberProjectRequestDto dto,
                                             @Parameter(hidden = true) @ApiIgnore Principal principal) {
        memberProjectService.updateProjectMember(projectMemberId, dto, getMemberId(principal));
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 프로젝트 회원 조회
     * READER, WRITER, ADMIN
     */
    @GetMapping("/projects/{project-id}/members/{member-id}")
    @Operation(summary = "프로젝트 회원 조회", description = "프로젝트 회원을 조회합니다.", tags = "member_project")
    public ResponseEntity<MemberProjectResponseDto> getProjectMember(@Parameter(description = "프로젝트식별자", required = true, example = "1") @PathVariable("project-id") Long projectId,
                                           @Parameter(description = "회원식별자", required = true, example = "1") @PathVariable("member-id") Long memberId) {
        MemberProjectResponseDto response = memberProjectService.findProjectMember(projectId, memberId);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * 프로젝트 회원 목록 조회
     * READER, WRITER, ADMIN
     */
    @GetMapping("/projects/{project-id}/members")
    @Operation(summary = "프로젝트 회원 목록 조회", description = "프로젝트 회원이 프로젝트 회원 목록을 조회합니다.", tags = "member_project")
    public ResponseEntity<SingleResponseDto<List<MemberProjectResponseDto>>> getProjectMembers(@Parameter(description = "프로젝트식별자", required = true, example = "1") @PathVariable("project-id") Long projectId) {
        List<MemberProjectResponseDto> projectMembers = memberProjectService.findProjectMembers(projectId);
        return new ResponseEntity(new SingleResponseDto<>(projectMembers), HttpStatus.OK);
    }

    /**
     * 자신의 프로젝트 나가기
     * READER, WRITER
     */
    @DeleteMapping("/projects/{project-id}/members")
    @Operation(summary = "프로젝트에서 나가기", description = "프로젝트 회원이 프로젝트에서 나갑니다. admin일 경우 금지되며, 권한 위임 후 나갈 수 있습니다.", tags = "member_project")
    public ResponseEntity deleteProjectMemberBySelf(@Parameter(description = "프로젝트식별자", required = true, example = "1") @PathVariable("project-id") Long projectId,
                                                    @Parameter(hidden = true) @ApiIgnore Principal principal) {
        memberProjectService.exitProjectMember(projectId, getMemberId(principal));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    private Long getMemberId(Principal principal) {
        return Optional.ofNullable(principal)
                .map(p -> Long.parseLong(p.getName()))
                .orElse(null);
    }

}
