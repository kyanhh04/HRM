//package com.vatek.hrmtool.controller;
//
//import com.vatek.hrmtool.dto.ProjectDto.ApprovalProjectDto;
//import com.vatek.hrmtool.dto.TimesheetDto.*;
//import com.vatek.hrmtool.enumeration.TimesheetStatus;
//import com.vatek.hrmtool.enumeration.WorkingType;
//import com.vatek.hrmtool.service.TaskService;
//import com.vatek.hrmtool.service.serviceImpl.ExportServiceImpl;
//import com.vatek.hrmtool.service.serviceImpl.ReportServiceImpl;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("/timesheets")
//public class TimeSheetController {
//    @Autowired
//    private ExportServiceImpl exportService;
//    @Autowired
//    private ReportServiceImpl reportService;
//    @Autowired
//    private TaskService taskService;
//    @PostMapping("/create-timesheet")
//    public ResponseEntity<?> createTask(@Valid @RequestBody CreateTaskDto createTaskDto){
//        CreateTaskDto dto = taskService.createTask(createTaskDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
//    }
//    @GetMapping("/user/{userId}/projects")
//    public ResponseEntity<?> getProjectsOfUser(@PathVariable Long userId){
//        List<ProjectOfUserDto> projectOfUserDto = taskService.getProjects(userId);
//        return ResponseEntity.ok(projectOfUserDto);
//    }
//    @PutMapping("/update-timesheet/{taskId}")
//    public ResponseEntity<?> updateTask(@Valid @RequestBody UpdateTaskDto updateTaskDto, Long taskId){
//        UpdateTaskDto dto = taskService.updateTask(updateTaskDto, taskId);
//        return ResponseEntity.ok(dto);
//    }
//    @GetMapping("/get-timesheet-weekly-by-user")
//    public ResponseEntity<?> getTasks(@RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate){
//        if(startDate == null || endDate == null){
//            LocalDate today = LocalDate.now();
//            DayOfWeek dayOfWeek = today.getDayOfWeek();
//            LocalDate monday = today.minusDays(dayOfWeek.getValue() - DayOfWeek.MONDAY.getValue());
//            LocalDate sunday = monday.plusDays(6);
//            startDate = monday;
//            endDate = sunday;
//        }
//        List<ListTaskGroupDto> tasksDto = taskService.getListTask(startDate, endDate);
//        return ResponseEntity.ok(tasksDto);
//    }
//    // // Code cũ - dùng Role ADMIN và PM
//    // @PreAuthorize("hasAnyRole('ADMIN', 'PM')")
//    // Code mới - dùng Position POSITION_ADMIN và POSITION_PM
//    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM')")
//    @GetMapping("/approvalList")
//    public ResponseEntity<?> getApprovalList(@RequestParam(required = false) YearMonth yearMonth,
//                                             @RequestParam(defaultValue = "ALL") WorkingType workingType,
//                                             @RequestParam(defaultValue = "PENDING") TimesheetStatus timesheetStatus
//                                             ) {
//            if(yearMonth == null){
//                yearMonth = YearMonth.now();
//        }
//        List<ApprovalProjectDto> approvalProjectDtos = taskService.getApprovalList(yearMonth, workingType, timesheetStatus);
//        return ResponseEntity.ok(approvalProjectDtos);
//    }
//    // // Code cũ - dùng Role ADMIN và PM
//    // @PreAuthorize("hasAnyRole('ADMIN', 'PM')")
//    // Code mới - dùng Position POSITION_ADMIN và POSITION_PM
//    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM')")
//    @PostMapping("/approval-timesheet/{taskId}")
//    public ResponseEntity<?> acceptTask(@PathVariable Long taskId){
//        taskService.acceptTask(TimesheetStatus.ACCEPTED, taskId);
//        return ResponseEntity.ok("Accept successfully");
//    }
//    // // Code cũ - dùng Role ADMIN và PM
//    // @PreAuthorize("hasAnyRole('ADMIN', 'PM')")
//    // Code mới - dùng Position POSITION_ADMIN và POSITION_PM
//    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM')")
//    @PostMapping("/reject-timesheet/{taskId}")
//    public ResponseEntity<?> rejectTask(@PathVariable Long taskId){
//        taskService.rejectTask(TimesheetStatus.REJECTED, taskId);
//        return ResponseEntity.ok("Reject successfully");
//    }
//    // // Code cũ - dùng Role ADMIN và PM
//    // @PreAuthorize("hasAnyRole('ADMIN', 'PM')")
//    // Code mới - dùng Position POSITION_ADMIN và POSITION_PM
//    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM')")
//    @GetMapping("/get-monthly-report")
//    public ResponseEntity<?> getReport(
//            @RequestParam(required = false) YearMonth yearMonth,
//            @RequestParam(defaultValue = "ALL") String projectName,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size){
//        if(yearMonth == null){
//            yearMonth = YearMonth.now();
//        }
//        Page<ReportDto> reports = taskService.getReport(yearMonth, projectName, page, size);
//        return ResponseEntity.ok(reports);
//    }
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<?> deleteTimesheet(Long id){
//        taskService.deleteTask(id);
//        return ResponseEntity.noContent().build();
//    }
//    // // Code cũ - dùng Role ADMIN và PM
//    // @PreAuthorize("hasAnyRole('ADMIN', 'PM')")
//    // Code mới - dùng Position POSITION_ADMIN và POSITION_PM
//    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM')")
//    @GetMapping("/export-timesheets-report")
//    public ResponseEntity<?> exportReports(
//            @RequestParam(required = false) Long projectId,
//            @RequestParam(required = false) YearMonth yearMonth) throws IOException {
//        if(yearMonth == null){
//            yearMonth = YearMonth.now();
//        }
//        List<ReportDto> reports = new ArrayList<>();
//        if (projectId != null) {
//            ReportDto report = reportService.getReportByProject(projectId, yearMonth);
//            if (report != null) {
//                reports.add(report);
//            }
//        }
//        else {
//            reports = reportService.getReports(yearMonth);
//        }
//        ByteArrayInputStream in = exportService.exportToExcel(reports, yearMonth);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reports.xlsx")
//                .contentType(MediaType.parseMediaType(
//                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
//                .body(new InputStreamResource(in));
//    }
//}
