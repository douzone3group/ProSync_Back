package com.douzone.prosync.project.controller;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.project.dto.ProjectRequest;
import com.douzone.prosync.project.dto.ProjectResponse;
import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.project.service.ProjectService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
@Tag(name="project", description = "프로젝트 API")
public class ProjectController {

    private final ProjectService projectService;

    // 프로젝트 생성
    @PostMapping
    @ApiOperation(value = "프로젝트 생성",notes = "프로젝트를 생성한다" ,tags = "project")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successfully retrieved", response = ProjectResponse.SimpleResponse.class),
            @ApiResponse(code = 404, message = "project not found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity createProject(@RequestBody ProjectRequest.PostDto dto) {
        Integer projectId=projectService.save(dto);
        return new ResponseEntity(new ProjectResponse.SimpleResponse(projectId), HttpStatus.CREATED);
    }

    // 프로젝트 단일 조회
    @GetMapping("/{project-id}")
    @ApiOperation(value = "프로젝트 단일 조회", notes = "프로젝트를 단일 조회 한다", tags = "project")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = ProjectResponse.GetProjectResponse.class),
            @ApiResponse(code = 404, message = "project not found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    public ResponseEntity getProject(@Parameter(description = "프로젝트 식별자", required = true, example = "1")
                                     @PathVariable("project-id") Integer projectId) {

        Project project = projectService.findProject(projectId);
        return new ResponseEntity(ProjectResponse.GetProjectResponse.of(project), HttpStatus.OK);
    }

    // 프로젝트 리스트 조회
    // TODO: sort - 종료 임박 순, 북마크 많은 순
    // TODO: filter - 내가 참여한 프로젝트, 내가 북마크 한 프로젝트,
    // TODO: search - 제목, 프로젝트 생성자,
    @GetMapping
    @ApiOperation(value = "프로젝트 전체 조회",notes = "프로젝트를 전체 조회 한다",tags = "project")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved",response = ProjectResponse.GetProjectsResponse.class),
            @ApiResponse(code = 404, message = "project not found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "조회할 페이지 번호", defaultValue = "1", example = "1"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "한페이지에 보여질 요소 개수", defaultValue = "10", example = "20")})
    public ResponseEntity<PageResponseDto<ProjectResponse.GetProjectsResponse>> getProjectList(
            @Parameter(hidden = true) @ApiIgnore @PageableDefault (size=8, sort="projectId", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Project> pages = projectService.findProjectList(pageable);
        List<Project> projects = pages.getContent();
        List<ProjectResponse.GetProjectsResponse> projectsResponse
                = projects.stream().map(ProjectResponse.GetProjectsResponse::of).collect(Collectors.toList());

        return new ResponseEntity(new PageResponseDto<>(projectsResponse,pages), HttpStatus.OK);
    }


    // 프로젝트 수정
    @PatchMapping("/{project-id}")
    @ApiOperation(value = "프로젝트 수정", notes = "프로젝트를 수정한다", tags = "project")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = ProjectResponse.SimpleResponse.class),
            @ApiResponse(code = 404, message = "project not found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    public ResponseEntity updateProject(@Parameter(description = "프로젝트 식별자", required = true, example = "1")
                                        @PathVariable("project-id") Integer projectId, @RequestBody ProjectRequest.PatchDto dto) {
        dto.setProjectId(projectId);
        projectService.update(dto);
        return new ResponseEntity(new ProjectResponse.SimpleResponse(projectId), HttpStatus.OK);
    }

    // 프로젝트 삭제
    @DeleteMapping("/{project-id}")
    @ApiOperation(value = "프로젝트 삭제", notes = "프로젝트를 소프트 삭제 한다", tags = "project")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "successfully retrieved", response = ProjectResponse.SimpleResponse.class),
            @ApiResponse(code = 404, message = "project not found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    public ResponseEntity deleteProject(@Parameter(description = "프로젝트 식별자", required = true, example = "1")
                                        @PathVariable("project-id") Integer projectId) {
        projectService.delete(projectId);
        return new ResponseEntity(new ProjectResponse.SimpleResponse(projectId), HttpStatus.NO_CONTENT);
    }


}
