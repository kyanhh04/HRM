//package com.vatek.hrmtool.service;
//
//import com.vatek.hrmtool.dto.ProjectDto.ApprovalProjectDto;
//import com.vatek.hrmtool.dto.TimesheetDto.*;
//import com.vatek.hrmtool.enumeration.TimesheetStatus;
//import com.vatek.hrmtool.enumeration.WorkingType;
//import org.springframework.data.domain.Page;
//
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.util.List;
//
//public interface TaskService {
//    CreateTaskDto createTask(CreateTaskDto dto);
//    List<ProjectOfUserDto> getProjects(Long id);
//    UpdateTaskDto updateTask(UpdateTaskDto dto, Long id);
//    List<ListTaskGroupDto> getListTask(LocalDate startDate, LocalDate endDate);
//    List<ApprovalProjectDto> getApprovalList(YearMonth date, WorkingType workingType, TimesheetStatus timesheetStatus);
//    void acceptTask(TimesheetStatus timesheetStatus, Long taskId);
//    void rejectTask(TimesheetStatus timesheetStatus, Long taskId);
//    Page<ReportDto> getReport(YearMonth yearMonth, String projectName, int page, int size);
//    void deleteTask(Long id);
//}
