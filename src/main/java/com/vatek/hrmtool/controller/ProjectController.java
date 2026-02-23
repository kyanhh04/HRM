//package com.vatek.hrmtool.controller;
//
//import com.vatek.hrmtool.dto.ProjectDto.*;
//import com.vatek.hrmtool.service.ProjectService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/project")
//public class ProjectController {
//    @Autowired
//    private ProjectService projectService;
//    // // Code cũ - dùng Role ADMIN và PM
//    // @PreAuthorize("hasAnyRole('ADMIN', 'PM')")
//    // Code mới - dùng Position POSITION_ADMIN và POSITION_PM
//    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM')")
//    @PostMapping("/create-project")
//    public ResponseEntity<?> createProject(@RequestBody CreateProjectDto createProjectDto){
//        CreateProjectDto dto = projectService.createProject(createProjectDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
//    }
//    // // Code cũ - dùng Role ADMIN và PM
//    // @PreAuthorize("hasAnyRole('ADMIN', 'PM')")
//    // Code mới - dùng Position POSITION_ADMIN và POSITION_PM
//    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM')")
//    @PatchMapping("/update-project/{id}")
//    public ResponseEntity<?> updateProject(@RequestBody UpdateProjectDto updateProjectDto, @PathVariable Long id){
//        UpdateProjectDto dto = projectService.updateProject(updateProjectDto, id);
//        return ResponseEntity.ok(dto);
//    }
//    // // Code cũ - dùng Role ADMIN và PM
//    // @PreAuthorize("hasAnyRole('ADMIN', 'PM')")
//    // Code mới - dùng Position POSITION_ADMIN và POSITION_PM
//    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM')")
//    @PatchMapping("/add-member/{id}")
//    public ResponseEntity<?> addMember(@RequestBody ProjectEmployeeDto projectEmployeeDto, @PathVariable Long id){
//        projectService.addMember(id, projectEmployeeDto);
//        return ResponseEntity.ok("Add member to project successfully");
//    }
//    // // Code cũ - dùng Role ADMIN và PM
//    // @PreAuthorize("hasAnyRole('ADMIN', 'PM')")
//    // Code mới - dùng Position POSITION_ADMIN và POSITION_PM
//    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM')")
//    @PatchMapping("/remove-member/id")
//    public ResponseEntity<?> deleteMember(@RequestBody ProjectEmployeeDto projectEmployeeDto, @PathVariable Long id){
//        projectService.deleteMember(id, projectEmployeeDto);
//        return ResponseEntity.noContent().build();
//    }
//    @GetMapping("/get-all-projects/new")
//    public ResponseEntity<?> getListProject(
//            @RequestParam(defaultValue = "") String keyword,
//            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
//            @RequestParam(defaultValue = "startDate") String startDate,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size
//            ){
//        Page<GetListProjectDto> allProject = projectService.getAllProject(keyword, direction, startDate, page, size);
//        return ResponseEntity.ok(allProject);
//    }
//    @GetMapping("/get-project-by-id/{id}")
//    public ResponseEntity<?> getProjectDetail(@PathVariable Long id){
//        ProjectDetailDto projectDetailDto = projectService.getProjectDetail(id);
//        return ResponseEntity.ok(projectDetailDto);
//    }
//    @DeleteMapping("/projects/{id}")
//    public ResponseEntity<?> deleteProject(@PathVariable Long id){
//        projectService.deleteProject(id);
//        return ResponseEntity.noContent().build();
//    }
//    @GetMapping("/get-all-projects")
//    public ResponseEntity<?> getListProjects(
//            @RequestParam(defaultValue = "") String projectName,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size
//    ){
//        Page<GetListProjectDto> allProject = projectService.getAllProjects(projectName, page, size);
//        return ResponseEntity.ok(allProject);
//    }
////    @GetMapping("get-user-project")
////    public ResponseEntity<?> getUserProject()
////    @GetMapping("get-pm-project")
////    public ResponseEntity<?> getPmProject()
//}
