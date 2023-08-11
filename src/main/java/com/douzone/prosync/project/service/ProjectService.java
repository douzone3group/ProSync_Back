package com.douzone.prosync.project.service;


import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;


    public void save(Project project) {
        projectRepository.createProject(project);
    }
}
