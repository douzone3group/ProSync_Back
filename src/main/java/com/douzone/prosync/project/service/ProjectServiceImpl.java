package com.douzone.prosync.project.service;


import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.project.dto.request.ProjectPatchDto;
import com.douzone.prosync.project.dto.request.ProjectPostDto;
import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.project.repository.ProjectJpaRepository;
import com.douzone.prosync.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final ProjectJpaRepository projectJpaRepository;


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

    // 프로젝트 종료 임박 순 정렬
    public Page<Project> findProjectsSortedByEndDateAsc(Pageable pageable) {
        return projectJpaRepository.findAllByOrderByEndDateAsc(pageable);
    }

    // 프로젝트 종료 임박 순 반대 정렬
    public Page<Project> findProjectsSortedByEndDateDesc(Pageable pageable) {
        return projectJpaRepository.findAllByOrderByEndDateDesc(pageable);
    }

    //프로젝트 이름 검색 필터
    public Page<Project> findProjectsByName(String name, Pageable pageable) {
        return projectJpaRepository.findByNameContaining(name, pageable);
    }
}
