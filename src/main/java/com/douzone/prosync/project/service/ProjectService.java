package com.douzone.prosync.project.service;

import com.douzone.prosync.project.dto.request.ProjectPatchDto;
import com.douzone.prosync.project.dto.request.ProjectPostDto;
import com.douzone.prosync.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

    public Integer save(ProjectPostDto dto);

    public void update(ProjectPatchDto dto);

    public void delete(Integer projectId);

    Project findProject(Integer projectId);

    Page<Project> findProjectList(Pageable pageable);

    // 프로젝트 종료 임박 순 정렬
    Page<Project> findProjectsSortedByEndDateAsc(Pageable pageable);

    Page<Project> findProjectsSortedByEndDateDesc(Pageable pageable);

    Page<Project> findProjectsByName(String name, Pageable pageable);




    List<Project> findByMemberIdAndIsDeletedNull(Long memberId, int offset, int size);


    long countByMemberId(Long memberId);
}
