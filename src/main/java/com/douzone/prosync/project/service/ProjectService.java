package com.douzone.prosync.project.service;

import com.douzone.prosync.project.dto.request.ProjectPatchDto;
import com.douzone.prosync.project.dto.request.ProjectPostDto;
import com.douzone.prosync.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

     Integer save(ProjectPostDto dto);

     void update(ProjectPatchDto dto);

     void delete(Integer projectId);

     Project findProject(Integer projectId);

     Page<Project> findProjectList(Pageable pageable);

     String createInviteLink(Integer projectId);

    // 프로젝트_회원 테이블에 추가
     void createProjectMember(Long memberId, String inviteCode);
}
