package com.douzone.prosync.project.mapper;

import com.douzone.prosync.project.dto.ProjectRequest;
import com.douzone.prosync.project.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {


    Project postDtoToProject(ProjectRequest.PostDto dto);
}
