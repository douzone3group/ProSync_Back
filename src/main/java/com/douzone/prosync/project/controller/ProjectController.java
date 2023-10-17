package com.douzone.prosync.project.controller;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.project.dto.request.ProjectPatchDto;
import com.douzone.prosync.project.dto.request.ProjectPostDto;
import com.douzone.prosync.project.dto.request.ProjectSearchCond;
import com.douzone.prosync.project.dto.response.GetProjectResponse;
import com.douzone.prosync.project.dto.response.GetProjectsResponse;
import com.douzone.prosync.project.dto.response.ProjectSimpleResponse;
import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.project.entity.ProjectWithBookmark;
import com.douzone.prosync.project.service.ProjectService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.security.Principal;
import java.util.Optional;

import static com.douzone.prosync.constant.ConstantPool.DEFAULT_PAGE_SIZE;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name="project", description = "프로젝트 API")
public class ProjectController {

    private final ProjectService projectService;

    /**
     * 프로젝트 생성
     * LOGIN USER
     */
    @PostMapping("/projects")
    @ApiOperation(value = "프로젝트 생성",notes = "프로젝트를 생성한다" ,tags = "project")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successfully retrieved", response = ProjectSimpleResponse.class),
            @ApiResponse(code = 404, message = "project not found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity createProject(@RequestBody @Valid ProjectPostDto dto, Principal principal) {
        Long projectId = projectService.save(dto, Long.parseLong(principal.getName()));
        return new ResponseEntity(new ProjectSimpleResponse(projectId), HttpStatus.CREATED);
    }

    /**
     * 프로젝트 단일 조회
     */
    @GetMapping("/projects/{project-id}")
    @ApiOperation(value = "프로젝트 단일 조회", notes = "프로젝트를 단일 조회 한다", tags = "project")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = GetProjectResponse.class),
            @ApiResponse(code = 404, message = "project not found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    public ResponseEntity<GetProjectResponse> getProject(@Parameter(description = "프로젝트 식별자", required = true, example = "1")
                                     @PathVariable("project-id") Long projectId) {

        Project project = projectService.findProject(projectId);
        return new ResponseEntity(GetProjectResponse.of(project), HttpStatus.OK);
    }

    /**
     * 프로젝트 수정
     * ADMIN
     */
    @PatchMapping("/projects/{project-id}")
    @ApiOperation(value = "프로젝트 수정", notes = "프로젝트를 수정한다", tags = "project")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = ProjectSimpleResponse.class),
            @ApiResponse(code = 404, message = "project not found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    public ResponseEntity updateProject(@Parameter(description = "프로젝트 식별자", required = true, example = "1")
                                        @PathVariable("project-id") Long projectId, @RequestBody @Valid ProjectPatchDto dto,
                                        @Parameter(hidden = true) @ApiIgnore Principal principal) {
        dto.setProjectId(projectId);
        projectService.update(dto,getMemberId(principal));
        return new ResponseEntity(new ProjectSimpleResponse(projectId), HttpStatus.OK);
    }

    /**
     * 프로젝트 삭제
     * ADMIN
     */
    @DeleteMapping("/projects/{project-id}")
    @ApiOperation(value = "프로젝트 삭제", notes = "프로젝트를 소프트 삭제 한다", tags = "project")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "successfully retrieved", response = ProjectSimpleResponse.class),
            @ApiResponse(code = 404, message = "project not found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    public ResponseEntity deleteProject(@Parameter(description = "프로젝트 식별자", required = true, example = "1")
                                        @PathVariable("project-id") @Positive Long projectId,
                                        @Parameter(hidden = true) @ApiIgnore Principal principal) {
        projectService.delete(projectId, getMemberId(principal));
        return new ResponseEntity(new ProjectSimpleResponse(projectId), HttpStatus.NO_CONTENT);
    }

    /**
     * 프로젝트 전체 조회
     */
    @GetMapping("/projects")
    @ApiOperation(value = "프로젝트 전체 조회", notes = "프로젝트를 전체 조회 한다", tags = "project")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = GetProjectsResponse.class),
            @ApiResponse(code = 404, message = "project not found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "조회할 페이지 번호", defaultValue = "1", example = "1"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "한페이지에 보여질 요소 개수", defaultValue = "10", example = "20"),
            @ApiImplicitParam(name = "search", dataType = "string", paramType = "query", value = "검색 키워드 (프로젝트제목, 생성자)", example = "검색 키워드"),
            @ApiImplicitParam(name = "bookmark", dataType = "boolean", paramType = "query", value = "북마크 여부", example = "true"),
            @ApiImplicitParam(name = "sort", dataType = "string", paramType = "query", value = "정렬기준 (최신순 - 기본, 마감임박순)", defaultValue = "latest", example = "latest or endDate")}
    )
    public ResponseEntity<PageResponseDto<GetProjectsResponse>> getProjectList(
            @Parameter(hidden = true) @ApiIgnore @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean bookmark,
            @RequestParam(required = false) String sort,
            Principal principal) {
        Long memberId = principal != null ? Long.parseLong(principal.getName()) : null;
        ProjectSearchCond searchCond = new ProjectSearchCond(search, bookmark, sort, memberId);
        PageResponseDto<GetProjectsResponse> response = projectService.findAll(searchCond, pageable);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * 내 프로젝트 목록 조회
     * LOGIN USER
     */
    @GetMapping("/user/myprojects")
    @ApiOperation(value = "내 프로젝트 조회", notes = "내가 속한 프로젝트를 조회합니다.", tags = "project")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = GetProjectsResponse.class),
            @ApiResponse(code = 404, message = "project not found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    public ResponseEntity<PageResponseDto<GetProjectsResponse>> getMemberProjects(
            @Parameter(hidden = true) @ApiIgnore @PageableDefault (size = 8) Pageable pageable,
            @ApiIgnore Principal principal,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean bookmark,
            @RequestParam(required = false) String sort
            ) {
        Long memberId = principal != null ? Long.parseLong(principal.getName()) : null;
        ProjectSearchCond searchCond = new ProjectSearchCond(search, bookmark, sort, memberId);
        PageResponseDto<GetProjectsResponse> response = projectService.findMyProjects(searchCond, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    private Long getMemberId(Principal principal) {
        return Optional.ofNullable(principal)
                .map(p -> Long.parseLong(p.getName()))
                .orElse(null);
    }

}