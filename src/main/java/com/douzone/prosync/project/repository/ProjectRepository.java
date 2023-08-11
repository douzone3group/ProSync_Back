package com.douzone.prosync.project.repository;

import com.douzone.prosync.project.dto.ProjectRequest;
import com.douzone.prosync.project.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProjectRepository {

    private final ProjectMybatisMapper projectMybatisMapper;

    // 프로젝트 생성
    public Project createProject(Project project) {
        projectMybatisMapper.insertProject(project);
        return project;
    }
   // 프로젝트 목록 조회
   public List<Project> findAll(ProjectRequest.PostDto dto) {
       return projectMybatisMapper.findAllProjects();
   }


    // 프로젝트 수정
    public Project updateProject(Long id, Project project) {
        projectMybatisMapper.updateProject(project);
        return project;
    }

    // 프로젝트 삭제
    public void deleteProject(Long id) {
        projectMybatisMapper.deleteProject(id);
    }
}
