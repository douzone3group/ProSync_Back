package com.douzone.prosync.project.repository;

import com.douzone.prosync.project.dto.request.ProjectPatchDto;
import com.douzone.prosync.project.dto.request.ProjectPostDto;
import com.douzone.prosync.project.entity.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProjectRepository {

    private final ProjectMybatisMapper projectMybatisMapper;

    // 프로젝트 생성
    public void createProject(ProjectPostDto dto) {

        projectMybatisMapper.createProject(dto);


    }

    //프로젝트 단일 조회
    public Optional<Project> findById(Integer projectId){
        return projectMybatisMapper.findProjectById(projectId);
    }

   // 프로젝트 목록 조회
    public List<Project> findAll(ProjectPostDto dto) {
        return projectMybatisMapper.findAllProjects();
    }


    // 프로젝트 수정
    public void updateProject(ProjectPatchDto dto) {
        projectMybatisMapper.updateProject(dto);

    }

    // 프로젝트 삭제
    public void deleteProject(Integer projectId) {
        projectMybatisMapper.deleteProject(projectId);
    }
}
