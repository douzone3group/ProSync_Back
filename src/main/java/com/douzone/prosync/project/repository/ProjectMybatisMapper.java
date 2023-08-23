package com.douzone.prosync.project.repository;
import com.douzone.prosync.project.dto.request.ProjectPatchDto;
import com.douzone.prosync.project.dto.request.ProjectPostDto;
import com.douzone.prosync.project.entity.Project;
import org.apache.ibatis.annotations.Mapper;



import java.util.List;
import java.util.Optional;

@Mapper
public interface ProjectMybatisMapper {

    // 프로젝트 목록 조회
    List<Project> findAllProjects();

    // 프로젝트 단일 조회
    Optional<Project> findProjectById(Integer projectId);

    // 프로젝트 생성
    void createProject(ProjectPostDto dto);

    // 프로젝트 수정
    void updateProject(ProjectPatchDto dto);

    // 프로젝트 삭제
    void deleteProject(Integer projectId);
}
