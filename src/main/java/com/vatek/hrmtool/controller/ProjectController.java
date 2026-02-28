package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.dto.ProjectDto.*;
import com.vatek.hrmtool.service.ProjectService;
import com.vatek.hrmtool.service.serviceImpl.UserOldPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PreAuthorize("hasAnyAuthority('POSITION_ADMIN', 'POSITION_PM')")
    @PostMapping("/create-project")
    public ResponseEntity<?> createProject(@RequestBody CreateProjectDto createProjectDto){
        CreateProjectDto dto = projectService.createProject(createProjectDto);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PreAuthorize("hasAnyAuthority('POSITION_ADMIN', 'POSITION_HR')")
    @GetMapping("/get-all-projects")
    public ResponseEntity<Page<GetListProjectDto>> getListProjects(
            @RequestParam(defaultValue = "") String projectName,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "5") int limit
    ){
        return ResponseEntity.ok(projectService.getAllProjects(projectName, offset, limit));
    }

    @GetMapping("/get-pm-project")
    public ResponseEntity<ProjectPaginationResponse> getPmProject(@AuthenticationPrincipal UserOldPrinciple userPrinciple) {
        String userId = userPrinciple.getId();
        return ResponseEntity.ok(projectService.getPmProjects(userId));
    }

    @GetMapping("/get-project-by-id/{id}")
    public ResponseEntity<ProjectDetailDto> getProjectDetail(@PathVariable String id){
        ProjectDetailDto projectDetail = projectService.getProjectDetail(id);
        return ResponseEntity.ok(projectDetail);
    }

    @GetMapping("/get-user-project")
    public ResponseEntity<ProjectPaginationResponse> getUserProject(@AuthenticationPrincipal UserOldPrinciple userPrinciple) {
        String userId = userPrinciple.getId();
        return ResponseEntity.ok(projectService.getUserProjects(userId));
    }

    @PreAuthorize("hasAnyAuthority('POSITION_ADMIN', 'POSITION_PM')")
    @PatchMapping("/update-project/{id}")
    public ResponseEntity<Map<String, Object>> updateProject(@RequestBody UpdateProjectDto updateProjectDto, @PathVariable String id){
        UpdateProjectDto updatedProject = projectService.updateProject(updateProjectDto, id);
        Map<String, Object> response = new HashMap<>();
        response.put("projectId", id);
        response.put("updatedData", updatedProject);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('POSITION_ADMIN', 'POSITION_PM')")
    @PatchMapping("/remove-member/{id}")
    public ResponseEntity<Map<String, Object>> removeMember(
            @PathVariable String id,
            @RequestBody ProjectEmployeeDto projectEmployeeDto){
        projectService.deleteMember(id, projectEmployeeDto);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Remove member successfully");
        response.put("memberRemovedCount", projectEmployeeDto.getMembers().size());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyAuthority('POSITION_ADMIN', 'POSITION_PM')")
    @PatchMapping("/add-member/{id}")
    public ResponseEntity<Void> addMember(
            @PathVariable String id,
            @RequestBody ProjectEmployeeDto projectEmployeeDto){
        projectService.addMember(id, projectEmployeeDto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('POSITION_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id){
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
