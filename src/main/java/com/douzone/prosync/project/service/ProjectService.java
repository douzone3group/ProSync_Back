package com.douzone.prosync.project.service;


import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.project.dto.ProjectRequest;
import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.project.repository.ProjectJpaRepository;
import com.douzone.prosync.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectJpaRepository projectJpaRepository;


    // 프로젝트 생성
    public Integer save(ProjectRequest.PostDto dto) {
        dto.setCreatedAt(LocalDateTime.now());
        projectRepository.createProject(dto);
        return dto.getProjectId();
    }

    // 프로젝트 수정
    public void update(ProjectRequest.PatchDto dto) {
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
}
