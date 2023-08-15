package com.douzone.prosync.project.controller;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.project.dto.ProjectRequest;
import com.douzone.prosync.project.dto.ProjectResponse;
import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;


    // 프로젝트 단일 조회
    @GetMapping("/{project-id}")
    public ResponseEntity getProject(@PathVariable("project-id") Integer projectId) {
        Project project = projectService.findProject(projectId);
        return new ResponseEntity(ProjectResponse.GetProjectResponse.of(project), HttpStatus.OK);
    }

    // 프로젝트 리스트 조회
    // sort - 종료 임박 순, 북마크 많은 순
    // filter - 내가 참여한 프로젝트, 내가 북마크 한 프로젝트,
    // search - 제목, 프로젝트 생성자,
    @GetMapping
    public ResponseEntity getProjectList(@PageableDefault (size=8, sort="projectId", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Project> pages = projectService.findProjectList(pageable);
        List<Project> projects = pages.getContent();
        List<ProjectResponse.GetProjectsResponse> projectsResponse
                = projects.stream().map(ProjectResponse.GetProjectsResponse::of).collect(Collectors.toList());

        return new ResponseEntity(new PageResponseDto<>(projectsResponse,pages), HttpStatus.OK);
    }

    // 프로젝트 생성
    @PostMapping
    public ResponseEntity createProject(@RequestBody ProjectRequest.PostDto dto) {
        Integer projectId=projectService.save(dto);

        log.info("dto.getName={}", dto.getName());
        return new ResponseEntity(new ProjectResponse.SimpleResponse(projectId), HttpStatus.CREATED);
    }

    // 프로젝트 수정
    @PatchMapping("/{project-id}")
    public ResponseEntity updateProject(@PathVariable("project-id") Integer projectId,
                                @RequestBody ProjectRequest.PatchDto dto) {
        dto.setProjectId(projectId);
        projectService.update(dto);
        return new ResponseEntity(new ProjectResponse.SimpleResponse(projectId), HttpStatus.OK);
    }

    // 프로젝트 삭제
    @DeleteMapping("/{project-id}")
    public ResponseEntity deleteProject(@PathVariable("project-id") Integer projectId) {
        projectService.delete(projectId);
        return new ResponseEntity(new ProjectResponse.SimpleResponse(projectId), HttpStatus.NO_CONTENT);
    }


}
