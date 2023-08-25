package com.douzone.prosync.project.service;

import com.douzone.prosync.project.dto.request.ProjectPatchDto;
import com.douzone.prosync.project.dto.request.ProjectPostDto;
import com.douzone.prosync.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    public Integer save(ProjectPostDto dto);

    public void update(ProjectPatchDto dto);

    public void delete(Integer projectId);

    public Project findProject(Integer projectId);

    public Page<Project> findProjectList(Pageable pageable);

    // 프로젝트 종료 임박 순 정렬
    public Page<Project> findProjectsSortedByEndDateAsc(Pageable pageable);

    public Page<Project> findProjectsSortedByEndDateDesc(Pageable pageable);

    public Page<Project> findProjectsByName(String name, Pageable pageable);
}
