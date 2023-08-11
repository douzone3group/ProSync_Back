package com.douzone.prosync.project.repository;
import com.douzone.prosync.project.entity.Project;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface ProjectMybatisMapper {

    // 프로젝트 목록 조회
    List<Project> findAllProjects();

    // 프로젝트 단일 조회
    Project findProjectById(Integer id);

    // 프로젝트 생성
    void insertProject(Project project);

    // 프로젝트 수정
    int updateProject(Project project);

    // 프로젝트 삭제
    int deleteProject(Long id);
}
