package com.douzone.prosync.project.service;


import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.member_project.repository.MemberProjectMapper;
import com.douzone.prosync.project.dto.request.ProjectPatchDto;
import com.douzone.prosync.project.dto.request.ProjectPostDto;
import com.douzone.prosync.project.dto.request.ProjectSearchCond;
import com.douzone.prosync.project.dto.response.GetProjectsResponse;
import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.project.repository.ProjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final MemberProjectMapper memberProjectMapper;

    // 프로젝트 생성
    public Integer save(ProjectPostDto dto, Long memberId) {
        dto.setCreatedAt(LocalDateTime.now());

        projectMapper.createProject(dto);
        Integer projectId = dto.getProjectId();

        memberProjectMapper.saveProjectAdmin(dto.getProjectId(), memberId);
        return projectId;
    }

    // 프로젝트 수정
    public void update(ProjectPatchDto dto) {
        dto.setModifiedAt(LocalDateTime.now());
        Integer row = projectMapper.updateProject(dto);
        if (row < 1) {
            throw new ApplicationException(ErrorCode.PROJECT_NOT_FOUND);
        }

    }

    // 프로젝트 삭제 (소프트)
    public void delete(Integer projectId) {
        Integer row = projectMapper.deleteProject(projectId);
        if (row < 1) {
            throw new ApplicationException(ErrorCode.PROJECT_NOT_FOUND);
        }
    }

    //프로젝트 조회
    public Project findProject(Integer projectId) {
        Optional<Project> project = projectMapper.findById(projectId);
        return project.orElseThrow(() -> new ApplicationException(ErrorCode.PROJECT_NOT_FOUND));
    }

    // 프로젝트 리스트 조회
    public PageInfo<GetProjectsResponse> findAll(ProjectSearchCond searchCond, Pageable pageable) {
        int pageNum = pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber();
        PageHelper.startPage(pageNum, pageable.getPageSize());
        List<GetProjectsResponse> projectList = projectMapper.findAll(searchCond);
        return new PageInfo<>(projectList);
    }


    public PageInfo<GetProjectsResponse> findMyProjects(Long memberId, Pageable pageable) {
        int pageNum = pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber();
        PageHelper.startPage(pageNum, pageable.getPageSize());
        List<GetProjectsResponse> myProjects = projectMapper.findByMemberId(memberId);
        return new PageInfo<>(myProjects);
    }

}
