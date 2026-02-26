package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.ProjectDto.*;
import org.springframework.data.domain.Page;

public interface ProjectService {
    CreateProjectDto createProject(CreateProjectDto dto);
    UpdateProjectDto updateProject(UpdateProjectDto dto, String id);
    void addMember(String projectId, ProjectEmployeeDto dto);
    void deleteMember(String projectId, ProjectEmployeeDto dto);
    Page<GetListProjectDto> getAllProjects(String projectName, int offset, int limit);
    ProjectDetailDto getProjectDetail(String id);
    void deleteProject(String id);
    ProjectPaginationResponse getUserProjects(String userId);
    ProjectPaginationResponse getPmProjects(String userId);
}
