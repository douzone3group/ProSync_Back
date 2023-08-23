package com.douzone.prosync.project.service;


import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.project.dto.request.ProjectPatchDto;
import com.douzone.prosync.project.dto.request.ProjectPostDto;
import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.project.repository.ProjectJpaRepository;
import com.douzone.prosync.project.repository.ProjectRepository;
import com.douzone.prosync.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static com.douzone.prosync.constant.ConstantPool.PROJECT_INVITE_LINK_DURATION;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectJpaRepository projectJpaRepository;
    private final RedisService redisService;


    // 프로젝트 생성
    public Integer save(ProjectPostDto dto) {
        dto.setCreatedAt(LocalDateTime.now());
        projectRepository.createProject(dto);
        return dto.getProjectId();
    }

    // 프로젝트 수정
    public void update(ProjectPatchDto dto) {
        dto.setModifiedAt(LocalDateTime.now());
        projectRepository.updateProject(dto);
    }

    // 프로젝트 삭제 (소프트)
    public void delete(Integer projectId) {
        projectRepository.deleteProject(projectId);

    }

    //프로젝트 조회
    public Project findProject(Integer projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        return project.orElseThrow(() -> new ApplicationException(ErrorCode.PROJECT_NOT_FOUND));
    }

    // 프로젝트 리스트 조회
    public Page<Project> findProjectList(Pageable pageable) {
        return projectJpaRepository.findAllByIsDeletedNull(pageable);
    }

    // 프로젝트 초대 링크 생성
    public String createInviteLink(Integer projectId) {
        findProject(projectId);
        String result = redisService.get("invite_project:" + projectId);

        //초대 링크 존재할 경우 기존 링크 리턴
        if (result != null) {
            return redisService.get("invite_project:" + projectId);
        }
        return createInviteCodeForProject(projectId);
    }

    // 프로젝트_회원 테이블에 추가
    public void createProjectMember(Long memberId, String inviteCode) {
        String projectId = redisService.get("invite:" + inviteCode);
        if (projectId == null) {
            throw new ApplicationException(ErrorCode.PROJECT_LINK_NOT_FOUND);
        }
        projectRepository.saveProjectMember(memberId, Integer.parseInt(projectId));
    }

    private String createInviteCodeForProject(Integer projectId) {

        Random random = new Random();
        String inviteCode = "";

        for (int i = 0; i < 3; i++) {
            int idx = random.nextInt(25) + 65;
            inviteCode += (char) idx;
        }
        inviteCode += random.nextInt(9999) + 1000;

        redisService.set("invite_project:" + projectId, inviteCode, PROJECT_INVITE_LINK_DURATION);
        redisService.set("invite:" + inviteCode, projectId.toString(), PROJECT_INVITE_LINK_DURATION);
        return inviteCode;
    }
}
