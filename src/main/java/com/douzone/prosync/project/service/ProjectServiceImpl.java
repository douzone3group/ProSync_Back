package com.douzone.prosync.project.service;


import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.file.basic.BasicImage;
import com.douzone.prosync.file.dto.FileRequestDto;
import com.douzone.prosync.file.dto.FileResponseDto;
import com.douzone.prosync.file.entity.File;
import com.douzone.prosync.file.entity.FileInfo;
import com.douzone.prosync.file.service.FileService;
import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;
import com.douzone.prosync.member_project.entity.MemberProject;
import com.douzone.prosync.member_project.repository.MemberProjectMapper;
import com.douzone.prosync.project.dto.request.ProjectPatchDto;
import com.douzone.prosync.project.dto.request.ProjectPostDto;
import com.douzone.prosync.project.dto.request.ProjectSearchCond;
import com.douzone.prosync.project.dto.response.GetProjectsResponse;
import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.project.repository.ProjectMapper;
import com.douzone.prosync.task_status.dto.TaskStatusDto;
import com.douzone.prosync.task_status.service.TaskStatusService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final MemberProjectMapper memberProjectMapper;
    private final TaskStatusService taskStatusService;
    private final FileService fileService;

    // 프로젝트 생성
    public Long save(ProjectPostDto dto, Long memberId) {

        projectMapper.createProject(dto);
        Long projectId = dto.getProjectId();

        createDefaultTaskStatus(projectId);

        memberProjectMapper.saveProjectAdmin(dto.getProjectId(), memberId, MemberProject.MemberProjectStatus.ACTIVE);

        // 프로젝트 이미지 - fileId 값이 있는 경우
        if (dto.getFileId() != null) {
            File file = fileService.findFile(dto.getFileId());
            fileService.saveFileInfo(FileInfo.createFileInfo(FileInfo.FileTableName.PROJECT, projectId, file.getFileId()));
            dto.setProjectImage(file.getPath());
        } else { // 기본 프로젝트 이미지 세팅
            dto.setProjectImage(BasicImage.BASIC_PROJECT_IMAGE.getPath());
        }

        return projectId;
    }

    private void createDefaultTaskStatus(Long projectId) {
        taskStatusService.createTaskStatus(projectId, TaskStatusDto.PostDto.createNoStatus());
        taskStatusService.createTaskStatus(projectId, TaskStatusDto.PostDto.createInProgress());
        taskStatusService.createTaskStatus(projectId, TaskStatusDto.PostDto.createTodo());
        taskStatusService.createTaskStatus(projectId, TaskStatusDto.PostDto.createDone());
    }

    // 프로젝트 수정
    public void update(ProjectPatchDto dto) {
        Project findProject = findProject(dto.getProjectId());

        // 프로젝트 이미지 - fileId 값이 있는 경우
        if (dto.getFileId() != null) {

            // 프로젝트 이미지 세팅
            File file = fileService.findFile(dto.getFileId());
            fileService.saveFileInfo(FileInfo.createFileInfo(FileInfo.FileTableName.PROJECT, dto.getProjectId(), file.getFileId()));
            dto.setProjectImage(file.getPath());

            // 기본 프로젝트 이미지가 아니면 기존 file 삭제
            if (!findProject.getProjectImage().equals(BasicImage.BASIC_PROJECT_IMAGE.getPath())) {
                FileRequestDto projectImage = FileRequestDto.create(FileInfo.FileTableName.PROJECT, dto.getProjectId());
                FileResponseDto findProjectImage = fileService.findFilesByTableInfo(projectImage, false).get(0);
                fileService.delete(findProjectImage.getFileInfoId());
            }

        } else {
            dto.setProjectImage(findProject.getProjectImage());
        }

        projectMapper.updateProject(dto);
    }

    // 프로젝트 삭제 (소프트)
    public void delete(Long projectId) {
        Integer row = projectMapper.deleteProject(projectId);
        if (row < 1) {
            throw new ApplicationException(ErrorCode.PROJECT_NOT_FOUND);
        }
        fileService.deleteFileList(FileRequestDto.create(FileInfo.FileTableName.PROJECT, projectId));
    }

    //프로젝트 조회
    public Project findProject(Long projectId) {
        Optional<Project> project = projectMapper.findById(projectId);
        return project.orElseThrow(() -> new ApplicationException(ErrorCode.PROJECT_NOT_FOUND));
    }

    // 프로젝트 리스트 조회
    public PageResponseDto<GetProjectsResponse> findAll(ProjectSearchCond searchCond, Pageable pageable) {
        int pageNum = pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber();
        PageHelper.startPage(pageNum, pageable.getPageSize());

        List<GetProjectsResponse> projectList = projectMapper.findAll(searchCond);

        return getGetProjectsResponsePageResponseDto(projectList);
    }


    public PageResponseDto<GetProjectsResponse> findMyProjects(Long memberId, Pageable pageable) {
        int pageNum = pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber();
        PageHelper.startPage(pageNum, pageable.getPageSize());

        List<GetProjectsResponse> myProjects = projectMapper.findByMemberId(memberId);
        return getGetProjectsResponsePageResponseDto(myProjects);
    }

    private PageResponseDto<GetProjectsResponse> getGetProjectsResponsePageResponseDto(List<GetProjectsResponse> projects) {
        PageInfo<GetProjectsResponse> pageInfo = new PageInfo<>(projects);

        projects.forEach(project -> {
            List<MemberProjectResponseDto> projectMembers = memberProjectMapper.findProjectMembers(project.getProjectId());
            project.setProjectMembers(projectMembers);
        });

        return new PageResponseDto<>(pageInfo);
    }

    // 내 프로젝트 중 관리자인 프로젝트만 조회
    @Override
    public PageInfo<GetProjectsResponse> findMyProjectsPartOfAdmin(Long memberId, Pageable pageable) {
        int pageNum = pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber();
        PageHelper.startPage(pageNum, pageable.getPageSize());
        List<GetProjectsResponse> myProjects = projectMapper.findByMemberIdPartOfAdmin(memberId);
        myProjects = myProjects.stream().map(project -> {
            List<MemberProjectResponseDto> projectMembers = memberProjectMapper.findProjectMembers(project.getProjectId());
            project.setProjectMembers(projectMembers);
            return project;
        }).collect(Collectors.toList());
        return new PageInfo<>(myProjects);
    }
}
