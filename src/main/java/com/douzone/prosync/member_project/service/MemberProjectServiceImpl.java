package com.douzone.prosync.member_project.service;

import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.log.dto.LogConditionDto;
import com.douzone.prosync.log.logenum.LogCode;
import com.douzone.prosync.log.service.LogService;
import com.douzone.prosync.member_project.dto.MemberProjectRequestDto;
import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;
import com.douzone.prosync.member_project.entity.MemberProject;
import com.douzone.prosync.member_project.repository.MemberProjectMapper;
import com.douzone.prosync.member_project.status.ProjectMemberAuthority;
import com.douzone.prosync.notification.dto.NotificationConditionDto;
import com.douzone.prosync.notification.notienum.NotificationCode;
import com.douzone.prosync.notification.service.NotificationService;
import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.project.repository.ProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.ArrayList;
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

    private final NotificationService notificationService;

    private final LogService logService;

    private final ProjectMapper projectMapper;

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
        Long fromMemberId = projectMemberMapper.findAdminByProjectId(findProjectId);

        List<Long> memberIds = new ArrayList<>();
        memberIds.add(memberId);

        Project project = projectMapper.findById(findProjectId).get();

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

            // 알림 저장 및 전달
            notificationService.saveAndSendNotification(NotificationConditionDto.builder()
                    .fromMemberId(fromMemberId)
                    .code(NotificationCode.PROJECT_ASSIGNMENT)
                    .memberIds(memberIds)
                    .projectId(findProjectId)
                    .subject(project)
                    .build());

            notificationService.saveAndSendNotification(NotificationConditionDto.builder()
                    .memberId(memberId)
                    .fromMemberId(fromMemberId)
                    .code(NotificationCode.CHANGE_AUTHORITY)
                    .memberIds(memberIds)
                    .projectId(findProjectId)
                    .authority(ProjectMemberAuthority.READER)
                    .subject(project)
                    .build());

            // 로그 저장
            logService.saveLog(LogConditionDto.builder()
                    .fromMemberId(fromMemberId)
                    .code(LogCode.PROJECT_ASSIGNMENT)
                    .projectId(findProjectId)
                    .subject(project).build());

            logService.saveLog(LogConditionDto.builder()
                    .fromMemberId(fromMemberId)
                    .code(LogCode.CHANGE_AUTHORITY)
                    .projectId(findProjectId)
                    .memberId(memberId)
                    .build());

        } else {
            projectMemberMapper.saveProjectMember(memberId, findProjectId, MemberProject.MemberProjectStatus.ACTIVE);

            // 알림 저장 및 전달
            notificationService.saveAndSendNotification(NotificationConditionDto.builder()
                    .fromMemberId(fromMemberId)
                    .code(NotificationCode.PROJECT_ASSIGNMENT)
                    .memberIds(memberIds)
                    .projectId(findProjectId)
                    .subject(project)
                    .build());

            // 로그 저장
            logService.saveLog(LogConditionDto.builder()
                    .fromMemberId(fromMemberId)
                    .code(LogCode.PROJECT_ASSIGNMENT)
                    .projectId(findProjectId)
                    .subject(project).build());
        }

        return findProjectId;
    }


    // 프로젝트 회원 수정 (권한 수정)
    @Override
    public void updateProjectMember(Long projectMemberId, MemberProjectRequestDto dto, Long memberId) {

        Integer row = projectMemberMapper.updateAuthorityOfProjectMember(projectMemberId, dto.getAuthority());

        MemberProject memberProject = projectMemberMapper.findProjectMemberById(projectMemberId).orElse(null);

        Project project = projectMapper.findById(memberProject.getProjectId()).get();
        List<Long> membersInProject = projectMapper.findMembersInProject(memberProject.getProjectId());

        List<Long> memberIds = new ArrayList<>();
        memberIds.add(memberProject.getMemberId());


        if (row < 1) {
            throw new ApplicationException(ErrorCode.PROJECT_MEMBER_NOT_FOUND);
        }
        // 프로젝트 회원 권한을 ADMIN 으로 변경하는 경우 (위임)
        // -> 기존 ADMIN은 WRITER 권한 부여
        if (dto.getAuthority().equals(ProjectMemberAuthority.ADMIN)) {

            // 알림 저장 및 전달
            notificationService.saveAndSendNotification(NotificationConditionDto.builder()
                    .fromMemberId(memberId)
                    .code(NotificationCode.CHANGE_AUTHORITY)
                    .memberId(memberProject.getMemberId())
                    .memberIds(membersInProject)
                    .projectId(memberProject.getProjectId())
                    .subject(project)
                    .authority(dto.getAuthority()).build());


            // 로그 저장

            logService.saveLog(LogConditionDto.builder()
                    .fromMemberId(memberId)
                    .code(LogCode.CHANGE_AUTHORITY)
                    .memberId(memberProject.getMemberId())
                    .projectId(memberProject.getProjectId())
                    .authority(dto.getAuthority()).build());


            Long projectId = projectMemberMapper.findProjectByProjectMemberId(projectMemberId);
            Long memberProjectId = findProjectMember(projectId, memberId).getMemberProjectId();
            System.out.println(memberProjectId);
            projectMemberMapper.updateAuthorityOfProjectMember(memberProjectId, ProjectMemberAuthority.WRITER);


            // 알림 저장 및 전달
            notificationService.saveAndSendNotification(NotificationConditionDto.builder()
                    .fromMemberId(memberId)
                    .code(NotificationCode.CHANGE_AUTHORITY)
                    .memberId(memberId)
                    .memberIds(memberIds)
                    .projectId(memberProject.getProjectId())
                    .subject(project)
                    .authority(ProjectMemberAuthority.WRITER).build());



            // 로그 저장
            logService.saveLog(LogConditionDto.builder()
                    .fromMemberId(memberId)
                    .code(LogCode.CHANGE_AUTHORITY)
                    .memberId(memberId)
                    .projectId(memberProject.getProjectId())
                    .authority(ProjectMemberAuthority.WRITER).build());
        } else {


            // 알림 저장 및 전달
            notificationService.saveAndSendNotification(NotificationConditionDto.builder()
                    .fromMemberId(memberId)
                    .code(NotificationCode.CHANGE_AUTHORITY)
                    .memberId(memberProject.getMemberId())
                    .memberIds(memberIds)
                    .projectId(memberProject.getProjectId())
                    .subject(project)
                    .authority(dto.getAuthority()).build());


            // 로그 저장
            logService.saveLog(LogConditionDto.builder()
                    .fromMemberId(memberId)
                    .code(LogCode.CHANGE_AUTHORITY)
                    .memberId(memberProject.getMemberId())
                    .projectId(memberProject.getProjectId())
                    .authority(dto.getAuthority()).build());
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
    public void deleteProjectMember(Long projectMemberId, Long fromMemberId) {
        Integer row = projectMemberMapper.deleteProjectMember(projectMemberId, MemberProject.MemberProjectStatus.QUIT);
        if (row < 1) {
            throw new ApplicationException(ErrorCode.PROJECT_MEMBER_NOT_FOUND);
        }

        MemberProject memberProject = projectMemberMapper.findProjectMemberById(projectMemberId).get();
        List<Long> memberIds = new ArrayList<>();
        memberIds.add(memberProject.getMemberId());

        Project project = projectMapper.findById(memberProject.getProjectId()).get();

        // 알림 저장 및 전달
        notificationService.saveAndSendNotification(NotificationConditionDto.builder()
                .fromMemberId(fromMemberId)
                .code(NotificationCode.PROJECT_EXCLUDED)
                .memberIds(memberIds)
                .subject(project).build());

        // 로그 저장
        logService.saveLog(LogConditionDto.builder()
                .fromMemberId(fromMemberId)
                .code(LogCode.PROJECT_EXCLUDED)
                .memberId(memberProject.getMemberId())
                .projectId(project.getProjectId())
                .subject(project).build());

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
        Project project = projectMapper.findById(projectId).get();

        // 로그 저장
        logService.saveLog(LogConditionDto.builder()
                .fromMemberId(memberId)
                .code(LogCode.PROJECT_EXIT)
                .projectId(projectId)
                .subject(project).build());

    }


    private String createInviteCodeForProject(Long projectId) {

        String invitationCode = UUID.randomUUID().toString().replace("-", "");

        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set("invite_project:" + projectId, invitationCode, Duration.ofSeconds(PROJECT_INVITE_LINK_DURATION));
        values.set("invite:" + invitationCode, projectId.toString());

        return invitationCode;
    }

}
