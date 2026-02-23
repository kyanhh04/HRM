// package com.vatek.hrmtool.service.serviceImpl;

// import com.vatek.hrmtool.dto.TimesheetDto.DailyWorkingHoursDto;
// import com.vatek.hrmtool.dto.TimesheetDto.EmployeeInProjectDto;
// import com.vatek.hrmtool.dto.TimesheetDto.ReportDto;
// import com.vatek.hrmtool.entity.neww.ProjectEntity;
// import com.vatek.hrmtool.entity.neww.TaskEntity;
// import com.vatek.hrmtool.entity.neww.UserEntity;
// import com.vatek.hrmtool.enumeration.TimesheetStatus;
// import com.vatek.hrmtool.respository.ProjectRepository;
// import com.vatek.hrmtool.respository.TaskRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.time.LocalDate;
// import java.time.YearMonth;
// import java.util.*;

// @Service
// public class ReportServiceImpl {
//     @Autowired
//     private ProjectRepository projectRepository;
    
//     @Autowired
//     private TaskRepository taskRepository;

//     public List<ReportDto> getReports(YearMonth yearMonth) {
//         List<ReportDto> reports = new ArrayList<>();
//         List<ProjectEntity> projects = projectRepository.findAll();
//         LocalDate startOfMonth = yearMonth.atDay(1);
//         LocalDate endOfMonth = yearMonth.atEndOfMonth();
//         for (ProjectEntity project : projects) {
//             ReportDto reportDto = new ReportDto();
//             reportDto.setProjectName(project.getProjectName());
//             List<EmployeeInProjectDto> employees = new ArrayList<>();
//             for (UserEntity user : project.getUsers()) {
//                 EmployeeInProjectDto employeeDto = new EmployeeInProjectDto();
//                 employeeDto.setName(user.getName());
//                 List<TaskEntity> userTasks = taskRepository.findAll().stream()
//                     .filter(t -> t.getUser().getId().equals(user.getId()) 
//                             && t.getProject().getId().equals(project.getId())
//                             && t.getTimesheetStatus() == TimesheetStatus.ACCEPTED
//                             && !t.getDate().isBefore(startOfMonth)
//                             && !t.getDate().isAfter(endOfMonth))
//                     .toList();
//                 double totalNormal = 0;
//                 double totalBonus = 0;
//                 double totalOvertime = 0;
//                 for (TaskEntity task : userTasks) {
//                     switch (task.getWorkingType()) {
//                         case NORMAL:
//                             totalNormal += task.getWorkingHour();
//                             break;
//                         case OT_CLIENT:
//                             totalBonus += task.getWorkingHour();
//                             break;
//                         case OT:
//                             totalOvertime += task.getWorkingHour();
//                             break;
//                         default:
//                             break;
//                     }
//                 }
//                 employeeDto.setTotalNormalHours(totalNormal);
//                 employeeDto.setTotalBonusHours(totalBonus);
//                 employeeDto.setTotalOvertimeHours(totalOvertime);
//                 List<DailyWorkingHoursDto> dailyHours = new ArrayList<>();
//                 for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
//                     final LocalDate currentDate = date;
//                     DailyWorkingHoursDto dailyDto = new DailyWorkingHoursDto();
//                     dailyDto.setDate(currentDate);
//                     double totalHours = userTasks.stream()
//                         .filter(t -> t.getDate().equals(currentDate))
//                         .mapToDouble(TaskEntity::getWorkingHour)
//                         .sum();
//                     dailyDto.setHour(totalHours);
//                     dailyHours.add(dailyDto);
//                 }
//                 employeeDto.setDailyWorkingHourDtos(dailyHours);
//                 employees.add(employeeDto);
//             }

//             reportDto.setEmployees(employees);
//             reports.add(reportDto);
//         }
//         return reports;
//     }

//     public ReportDto getReportByProject(Long projectId, YearMonth yearMonth) {
//         Optional<ProjectEntity> projectOptional = projectRepository.findById(projectId);
//         if (projectOptional.isEmpty()) {
//             return null;
//         }
        
//         ProjectEntity project = projectOptional.get();
//         ReportDto reportDto = new ReportDto();
//         reportDto.setProjectName(project.getProjectName());
//         List<EmployeeInProjectDto> employees = new ArrayList<>();
        
//         LocalDate startOfMonth = yearMonth.atDay(1);
//         LocalDate endOfMonth = yearMonth.atEndOfMonth();
        
//         for (UserEntity user : project.getUsers()) {
//             EmployeeInProjectDto employeeDto = new EmployeeInProjectDto();
//             employeeDto.setName(user.getName());
//             List<TaskEntity> userTasks = taskRepository.findAll().stream()
//                 .filter(t -> t.getUser().getId().equals(user.getId()) 
//                         && t.getProject().getId().equals(project.getId())
//                         && t.getTimesheetStatus() == TimesheetStatus.ACCEPTED
//                         && !t.getDate().isBefore(startOfMonth)
//                         && !t.getDate().isAfter(endOfMonth))
//                 .toList();
//             double totalNormal = 0;
//             double totalBonus = 0;
//             double totalOvertime = 0;
//             for (TaskEntity task : userTasks) {
//                 switch (task.getWorkingType()) {
//                     case NORMAL:
//                         totalNormal += task.getWorkingHour();
//                         break;
//                     case OT_CLIENT:
//                         totalBonus += task.getWorkingHour();
//                         break;
//                     case OT:
//                         totalOvertime += task.getWorkingHour();
//                         break;
//                     default:
//                         break;
//                 }
//             }
//             employeeDto.setTotalNormalHours(totalNormal);
//             employeeDto.setTotalBonusHours(totalBonus);
//             employeeDto.setTotalOvertimeHours(totalOvertime);
//             List<DailyWorkingHoursDto> dailyHours = new ArrayList<>();
//             for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
//                 final LocalDate currentDate = date;
//                 DailyWorkingHoursDto dailyDto = new DailyWorkingHoursDto();
//                 dailyDto.setDate(currentDate);
//                 double totalHours = userTasks.stream()
//                     .filter(t -> t.getDate().equals(currentDate))
//                     .mapToDouble(TaskEntity::getWorkingHour)
//                     .sum();
//                 dailyDto.setHour(totalHours);
//                 dailyHours.add(dailyDto);
//             }
//             employeeDto.setDailyWorkingHourDtos(dailyHours);
//             employees.add(employeeDto);
//         }

//         reportDto.setEmployees(employees);
//         return reportDto;
//     }
// }
