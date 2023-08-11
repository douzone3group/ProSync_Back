package com.douzone.prosync.project.controller;

import com.douzone.prosync.project.dto.ProjectRequest;
import com.douzone.prosync.project.mapper.ProjectMapper;
import com.douzone.prosync.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/projects")
@ResponseBody
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    // 프로젝트 단일 조회
    @GetMapping("/{project-id}")
    public String getProject(@PathVariable("project-id") Integer projectId) {
        return "getProject";
    }

    // 프로젝트 리스트 조회
    @GetMapping
    public String getProjectList() {
        return "getProjectList";
    }

    // 프로젝트 생성
    @PostMapping
    public String createProject(@RequestBody ProjectRequest.PostDto dto) {
        projectService.save(projectMapper.postDtoToProject(dto));
        return "createProject";
    }

    // 프로젝트 수정
    @PatchMapping("/{project-id}")
    public String updateProject(@PathVariable("project-id") Integer projectId) {
        return "updateProject";
    }

    // 프로젝트 삭제
    @DeleteMapping("/{project-id}")
    public String deleteProject(@PathVariable("project-id") Integer projectId) {
        return "deleteProject";
    }


}
