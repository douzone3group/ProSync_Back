package com.douzone.prosync.member_project.service;

import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.member_project.dto.MemberProjectRequestDto;
import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;
import com.douzone.prosync.member_project.entity.MemberProject;
import com.douzone.prosync.member_project.repository.MemberProjectMapper;
import com.douzone.prosync.member_project.status.ProjectMemberAuthority;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.douzone.prosync.constant.ConstantPool.PROJECT_INVITE_LINK_DURATION;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberProjectServiceImpl implements MemberProjectService {

    private final RedisTemplate redisTemplate;
    private final MemberProjectMapper projectMemberMapper;

    // 프로젝트 초대 링크 생성
    @Override
    public String createInviteLink(Long projectId) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        String result = values.get("invite_project:" + projectId);
        return result != null ? result : createInviteCodeForProject(projectId);
    }

    // 프로젝트_회원 저장
    @Override
    public Long createProjectMember(Long memberId, String inviteCode) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        String projectId = values.get("invite:" + inviteCode);

        // 잘못된 링크일 경우 예외
        if (projectId == null) {
            throw new ApplicationException(ErrorCode.PROJECT_LINK_NOT_FOUND);
        }

        Long findProjectId = Long.parseLong(projectId);

        // 이미 해당 프로젝트 회원일 경우
        Optional<MemberProjectResponseDto> projectMember = projectMemberMapper.findProjectMember(findProjectId, memberId);
        if (projectMember.isPresent()) {
            MemberProject.MemberProjectStatus status = projectMember.get().getStatus();
            if (status.equals(MemberProject.MemberProjectStatus.ACTIVE)) {
                throw new ApplicationException(ErrorCode.PROJECT_MEMBER_EXISTS, "projectId : " + findProjectId);
            }
            projectMemberMapper.updateStatusOfProjectMember(findProjectId, memberId, MemberProject.MemberProjectStatus.ACTIVE);
            MemberProjectResponseDto findProjectMember = findProjectMember(findProjectId, memberId);
            projectMemberMapper.updateAuthorityOfProjectMember(findProjectMember.getMemberProjectId(), ProjectMemberAuthority.READER);
        } else {
            projectMemberMapper.saveProjectMember(memberId, findProjectId, MemberProject.MemberProjectStatus.ACTIVE);
        }

        return findProjectId;
    }


    // 프로젝트 회원 수정 (권한 수정)
    @Override
    public void updateProjectMember(Long projectMemberId, MemberProjectRequestDto dto, Long memberId) {

        Integer row = projectMemberMapper.updateAuthorityOfProjectMember(projectMemberId, dto.getAuthority());

        MemberProject memberProject = projectMemberMapper.findProjectMemberById(projectMemberId).orElse(null);




        if (row < 1) {
            throw new ApplicationException(ErrorCode.PROJECT_MEMBER_NOT_FOUND);
        }
        // 프로젝트 회원 권한을 ADMIN 으로 변경하는 경우 (위임)
        // -> 기존 ADMIN은 WRITER 권한 부여
        if (dto.getAuthority().equals(ProjectMemberAuthority.ADMIN)) {
            Long projectId = projectMemberMapper.findProjectByProjectMemberId(projectMemberId);
            Long memberProjectId = findProjectMember(projectId, memberId).getMemberProjectId();
            System.out.println(memberProjectId);
            projectMemberMapper.updateAuthorityOfProjectMember(memberProjectId, ProjectMemberAuthority.WRITER);
        }
    }

    // 프로젝트 회원 조회
    @Override
    public MemberProjectResponseDto findProjectMember(Long projectId, Long memberId) {
        return projectMemberMapper.findProjectMember(projectId, memberId).orElseThrow(() -> new ApplicationException(ErrorCode.PROJECT_MEMBER_NOT_FOUND));
    }

    // 프로젝트 회원 목록 조회
    @Override
    public List<MemberProjectResponseDto> findProjectMembers(Long projectId) {
        return projectMemberMapper.findProjectMembers(projectId);
    }

    // 회원에 해당하는 프로젝트 리스트 조회
    @Override
    public List<Long> findProjectIdsByMemberId(Long memberId) {
        return projectMemberMapper.findProjectIdsByMemberId(memberId);
    }

    // 프로젝트 회원 삭제
    @Override
    public void deleteProjectMember(Long projectMemberId) {
        Integer row = projectMemberMapper.deleteProjectMember(projectMemberId, MemberProject.MemberProjectStatus.QUIT);
        if (row < 1) {
            throw new ApplicationException(ErrorCode.PROJECT_MEMBER_NOT_FOUND);
        }
    }

    // 프로젝트 나가기
    @Override
    public void exitProjectMember(Long projectId, Long memberId) {
        MemberProjectResponseDto projectMember = findProjectMember(projectId, memberId);
        // admin일 경우 위임 후 나가기 가능
        if (projectMember.getAuthority().equals(ProjectMemberAuthority.ADMIN)) {
            throw new ApplicationException(ErrorCode.ACCESS_FORBIDDEN);
        }
        projectMemberMapper.updateStatusOfProjectMember(projectId, memberId, MemberProject.MemberProjectStatus.QUIT);
    }


    private String createInviteCodeForProject(Long projectId) {

        String invitationCode = UUID.randomUUID().toString().replace("-", "");

        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set("invite_project:" + projectId, invitationCode, Duration.ofSeconds(PROJECT_INVITE_LINK_DURATION));
        values.set("invite:" + invitationCode, projectId.toString());

        return invitationCode;
    }

}
