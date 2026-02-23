//package com.vatek.hrmtool.service;
//
//import com.vatek.hrmtool.dto.ProjectDto.*;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Sort;
//
//public interface ProjectService {
//    CreateProjectDto createProject(CreateProjectDto dto);
//    UpdateProjectDto updateProject(UpdateProjectDto dto, Long id);
//    void addMember(Long projectId, ProjectEmployeeDto dto);
//    void deleteMember(Long projectId, ProjectEmployeeDto dto);
//    Page<GetListProjectDto> getAllProject(String keyword, Sort.Direction direction, String startDate, int page, int size);
//    Page<GetListProjectDto> getAllProjects(String projectName, int page, int size);
//    ProjectDetailDto getProjectDetail(Long id);
//    void deleteProject(Long id);
//}
