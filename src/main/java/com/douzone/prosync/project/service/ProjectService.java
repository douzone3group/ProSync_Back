package com.douzone.prosync.project.service;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.project.dto.request.ProjectPatchDto;
import com.douzone.prosync.project.dto.request.ProjectPostDto;
import com.douzone.prosync.project.dto.request.ProjectSearchCond;
import com.douzone.prosync.project.dto.response.GetProjectsResponse;
import com.douzone.prosync.project.entity.Project;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    Long save(ProjectPostDto dto, Long memberId);

    void update(ProjectPatchDto dto);

    void delete(Long projectId);

    Project findProject(Long projectId);

    PageResponseDto<GetProjectsResponse> findAll(ProjectSearchCond searchCond, Pageable pageable);

    PageResponseDto<GetProjectsResponse> findMyProjects(Long memberId, Pageable pageable);

}