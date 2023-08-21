package com.douzone.prosync.project.repository;

import com.douzone.prosync.file.basic.BasicImage;
import com.douzone.prosync.project.dto.ProjectRequest;
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
    public void createProject(ProjectRequest.PostDto dto) {
        dto.setProjectImage(BasicImage.BASIC_PROJECT_IMAGE.getPath()); // 기본이미지 설정
        projectMybatisMapper.createProject(dto);

    }

    //프로젝트 단일 조회
    public Optional<Project> findById(Integer projectId){
        return projectMybatisMapper.findProjectById(projectId);
    }
   // 프로젝트 목록 조회
    public List<Project> findAll(ProjectRequest.PostDto dto) {
        return projectMybatisMapper.findAllProjects();
    }


    // 프로젝트 수정
    public void updateProject(ProjectRequest.PatchDto dto) {
        projectMybatisMapper.updateProject(dto);

    }

    // 프로젝트 삭제
    public void deleteProject(Integer projectId) {
        projectMybatisMapper.deleteProject(projectId);
    }
}
