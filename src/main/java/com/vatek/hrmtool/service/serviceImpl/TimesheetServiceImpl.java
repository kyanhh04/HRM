package com.vatek.hrmtool.service.serviceImpl;

import com.vatek.hrmtool.dto.TimesheetDto.CreateTaskDto;
import com.vatek.hrmtool.dto.TimesheetDto.DailyWorkingHoursDto;
import com.vatek.hrmtool.dto.TimesheetDto.DaysInMonthDto;
import com.vatek.hrmtool.dto.TimesheetDto.EmployeeInProjectDto;
import com.vatek.hrmtool.dto.TimesheetDto.ExportTimesheetReportDto;
import com.vatek.hrmtool.dto.TimesheetDto.GetTimesheetByDateDto;
import com.vatek.hrmtool.dto.TimesheetDto.GetTimesheetWeeklyDto;
import com.vatek.hrmtool.dto.TimesheetDto.ReportDto;
import com.vatek.hrmtool.dto.TimesheetDto.TimesheetApprovalTypeDto;
import com.vatek.hrmtool.dto.TimesheetDto.TimesheetCustomDataDto;
import com.vatek.hrmtool.dto.TimesheetDto.NewApprovalTimesheetDto;
import com.vatek.hrmtool.dto.TimesheetDto.TimesheetChildrenBaseDto;
import com.vatek.hrmtool.dto.TimesheetDto.TimesheetResponseDto;
import com.vatek.hrmtool.entity.Config;
import com.vatek.hrmtool.entity.ProjectOld;
import com.vatek.hrmtool.entity.TimesheetOld;
import com.vatek.hrmtool.entity.UserOld;
import com.vatek.hrmtool.enumeration.Position;
import com.vatek.hrmtool.respository.old.ConfigRepository;
import com.vatek.hrmtool.respository.old.ProjectOldRepository;
import com.vatek.hrmtool.respository.old.TimesheetOldRepository;
import com.vatek.hrmtool.respository.old.UserOldRepository;
import com.vatek.hrmtool.service.TimesheetService;
import com.vatek.hrmtool.util.TimesheetUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
public class TimesheetServiceImpl implements TimesheetService {

    @Autowired
    private TimesheetOldRepository timesheetRepository;

    @Autowired
    private UserOldRepository userRepository;

    @Autowired
    private ProjectOldRepository projectRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ExportServiceImpl exportService;

    @Override
    public TimesheetResponseDto createTimesheet(CreateTaskDto dto, String userId) {

        if (dto.getProject() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project is required");
        }

        LocalDate timesheetDate = dto.getDate();

        Config pendingStatus = configRepository.findByKeyAndValue("STATUS", "Pending")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Pending status not found"));

        Config typeConfig = configRepository.findById(dto.getType())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Working type not found"));

        Config normalType = configRepository.findByKeyAndValue("WORKINGTYPE", "Normal working hours")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Normal working type not found"));

        if (isWeekend(timesheetDate) && typeConfig.getId().equals(normalType.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot log normal working hours on weekend");
        }

        List<TimesheetOld> existingTasks = timesheetRepository.findAll().stream()
                .filter(t -> t.getUser().getId().equals(userId) && 
                            t.getDate().equals(timesheetDate) && 
                            !t.getStatus().getValue().equals("Rejected"))
                .toList();

        double totalExistingHours = existingTasks.stream()
                .mapToDouble(t -> t.getHours() != null ? t.getHours() : 0)
                .sum();

        if (typeConfig.getId().equals(normalType.getId())) {
            if (totalExistingHours + dto.getHours() > 8.0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Normal working hours exceeded limit");
            }
        }

        UserOld user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        ProjectOld project = projectRepository.findById(dto.getProject())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        TimesheetOld timesheet = new TimesheetOld();
        timesheet.setDate(timesheetDate);
        timesheet.setHours(dto.getHours());
        timesheet.setDetails(dto.getDetails());
        timesheet.setUser(user);
        timesheet.setProject(project);
        timesheet.setStatus(pendingStatus);
        timesheet.setType(typeConfig);
        TimesheetOld savedTimesheet = timesheetRepository.save(timesheet);

        TimesheetResponseDto result = new TimesheetResponseDto();
        result.setId(savedTimesheet.getId());
        result.setDate(savedTimesheet.getDate().atStartOfDay().toString());
        result.setHours(savedTimesheet.getHours());
        result.setDetails(savedTimesheet.getDetails());
        result.setUser(savedTimesheet.getUser().getId());
        result.setProject(savedTimesheet.getProject().getId());
        result.setStatus(savedTimesheet.getStatus().getId());
        result.setType(savedTimesheet.getType().getId());
        result.setCreatedAt(savedTimesheet.getCreatedTime().toString());
        result.setUpdatedAt(savedTimesheet.getModifiedTime().toString());
        return result;
    }

    @Override
    public TimesheetResponseDto deleteTimesheet(String id) {
        Optional<TimesheetOld> optionalTimesheet = timesheetRepository.findById(id);
        if (optionalTimesheet.isEmpty()) {
            return null;
        }
        
        TimesheetOld timesheet = optionalTimesheet.get();
        TimesheetResponseDto response = new TimesheetResponseDto();
        response.setId(timesheet.getId());
        response.setDate(timesheet.getDate().atStartOfDay().toString());
        response.setHours(timesheet.getHours());
        response.setDetails(timesheet.getDetails());
        response.setUser(timesheet.getUser().getId());
        response.setProject(timesheet.getProject().getId());
        response.setStatus(timesheet.getStatus().getId());
        response.setType(timesheet.getType().getId());
        response.setCreatedAt(timesheet.getCreatedTime().toString());
        response.setUpdatedAt(timesheet.getModifiedTime().toString());
        
        timesheetRepository.delete(timesheet);
        return response;
    }

    @Override
    public Map<String, Object> findTimesheetByWeekly(String userId, GetTimesheetWeeklyDto params) {
        LocalDate startDate = params.getStartDate();
        LocalDate endDate = params.getEndDate();
        Map<String, Object> weeklyData = new LinkedHashMap<>();
        List<TimesheetOld> timesheets = timesheetRepository.findAll().stream()
                .filter(t -> t.getUser().getId().equals(userId) &&
                            !t.getDate().isBefore(startDate) &&
                            !t.getDate().isAfter(endDate))
                .toList();

        Map<String, List<TimesheetOld>> weekGroups = new LinkedHashMap<>();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        for (TimesheetOld timesheet : timesheets) {
            int weekNumber = timesheet.getDate().get(weekFields.weekOfYear());
            String weekKey = "Week " + weekNumber;
            weekGroups.computeIfAbsent(weekKey, k -> new ArrayList<>()).add(timesheet);
        }
        for (Map.Entry<String, List<TimesheetOld>> weekEntry : weekGroups.entrySet()) {
            Map<String, Object> weekData = new LinkedHashMap<>();
            List<Map<String, Object>> items = new ArrayList<>();
            double totalHours = 0;
            for (TimesheetOld timesheet : weekEntry.getValue()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("status", timesheet.getStatus().getValue());
                item.put("projectName", timesheet.getProject().getProjectName());
                item.put("type", timesheet.getType().getValue());
                item.put("hours", timesheet.getHours());
                item.put("details", timesheet.getDetails());
                items.add(item);
                if (!timesheet.getStatus().getValue().equals("Rejected")) {
                    totalHours += timesheet.getHours() != null ? timesheet.getHours() : 0;
                }
            }
            weekData.put("date", weekEntry.getValue().get(0).getDate());
            weekData.put("total", totalHours);
            weekData.put("items", items);
            weeklyData.put(weekEntry.getKey(), weekData);
        }
        return weeklyData;
    }

    @Override
    public List<NewApprovalTimesheetDto> listTimesheetByMonth(UserOldPrinciple requestUser, GetTimesheetByDateDto params) {
        LocalDate dateInput = params.getDate();
        YearMonth yearMonth = YearMonth.from(dateInput);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List<TimesheetOld> timesheets = timesheetRepository.findAll();
        List<TimesheetOld> filtered = timesheets.stream()
                .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
                .toList();
        if (params.getWorkingType() != null && !params.getWorkingType().isEmpty()) {
            filtered = filtered.stream()
                    .filter(t -> t.getType() != null && t.getType().getValue().equals(params.getWorkingType()))
                    .toList();
        }
        if (params.getStatus() != null && !params.getStatus().isEmpty()) {
            filtered = filtered.stream()
                    .filter(t -> t.getStatus() != null && t.getStatus().getValue().equals(params.getStatus()))
                    .toList();
        }
        boolean isPM = requestUser.getPositions() != null &&
                requestUser.getPositions().contains(Position.Code.PM) &&
                !(requestUser.getPositions().contains(Position.Code.ADMIN) ||
                  requestUser.getPositions().contains(Position.Code.HR));

        if (isPM) {
            List<ProjectOld> userProjects = projectRepository.findAll().stream()
                    .filter(p -> p.getProjectManager() != null && 
                               p.getProjectManager().getId().equals(requestUser.getId()))
                    .toList();
            List<String> projectIds = userProjects.stream()
                    .map(ProjectOld::getId)
                    .toList();
            
            filtered = filtered.stream()
                    .filter(t -> t.getProject() != null && projectIds.contains(t.getProject().getId()))
                    .toList();
        }
        filtered = filtered.stream()
                .sorted(Comparator.comparing(TimesheetOld::getDate))
                .toList();
        List<TimesheetApprovalTypeDto> approvalDtos = new ArrayList<>();
        for (TimesheetOld timesheet : filtered) {
            TimesheetApprovalTypeDto dto = getTimesheetApprovalTypeDto(timesheet);
            approvalDtos.add(dto);
        }
        return TimesheetUtils.handleTimeSheets(approvalDtos, dateInput);
    }

    private static TimesheetApprovalTypeDto getTimesheetApprovalTypeDto(TimesheetOld timesheet) {
        TimesheetApprovalTypeDto dto = new TimesheetApprovalTypeDto();
        dto.setId(timesheet.getId());
        dto.setDate(timesheet.getDate());
        dto.setDetails(timesheet.getDetails());
        dto.setHours(timesheet.getHours());
        if (timesheet.getStatus() != null) {
            TimesheetApprovalTypeDto.ConfigDto statusDto = new TimesheetApprovalTypeDto.ConfigDto();
            statusDto.setId(timesheet.getStatus().getId());
            statusDto.setValue(timesheet.getStatus().getValue());
            dto.setStatus(statusDto);
        }
        if (timesheet.getType() != null) {
            TimesheetApprovalTypeDto.ConfigDto typeDto = new TimesheetApprovalTypeDto.ConfigDto();
            typeDto.setId(timesheet.getType().getId());
            typeDto.setValue(timesheet.getType().getValue());
            dto.setType(typeDto);
        }
        if (timesheet.getUser() != null) {
            TimesheetApprovalTypeDto.UserDto userDto = new TimesheetApprovalTypeDto.UserDto();
            userDto.setId(timesheet.getUser().getId());
            userDto.setFullName(timesheet.getUser().getFullName());
            if (timesheet.getUser().getAvatar() != null) {
                userDto.setAvatar(timesheet.getUser().getAvatar().getSrc());
            }
            dto.setUser(userDto);
        }
        if (timesheet.getProject() != null) {
            TimesheetApprovalTypeDto.ConfigDto projectDto = new TimesheetApprovalTypeDto.ConfigDto();
            projectDto.setId(timesheet.getProject().getId());
            projectDto.setValue(timesheet.getProject().getProjectName());
            dto.setProject(projectDto);
        }
        return dto;
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    @Override
    public ByteArrayInputStream exportTimesheetReport(ExportTimesheetReportDto params) throws IOException {
        LocalDate dateInput = params.getDate();
        YearMonth yearMonth = YearMonth.from(dateInput);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        
        // Get timesheets for the project within the month
        List<TimesheetOld> timesheets = timesheetRepository.findAll().stream()
                .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
                .filter(t -> t.getProject() != null && t.getProject().getId().equals(params.getProject()))
                .filter(t -> t.getStatus() != null && "Accepted".equals(t.getStatus().getValue()))
                .toList();
        
        // Get project info
        ProjectOld project = projectRepository.findById(params.getProject())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        
        // Build report DTO
        ReportDto reportDto = new ReportDto();
        reportDto.setProjectName(project.getProjectName());
        
        // Group timesheets by user
        Map<String, List<TimesheetOld>> timesheetsByUser = timesheets.stream()
                .filter(t -> t.getUser() != null)
                .collect(java.util.stream.Collectors.groupingBy(t -> t.getUser().getId()));
        
        List<EmployeeInProjectDto> employees = new ArrayList<>();
        for (Map.Entry<String, List<TimesheetOld>> entry : timesheetsByUser.entrySet()) {
            List<TimesheetOld> userTimesheets = entry.getValue();
            if (userTimesheets.isEmpty()) continue;
            
            UserOld user = userTimesheets.get(0).getUser();
            EmployeeInProjectDto employeeDto = new EmployeeInProjectDto();
            employeeDto.setName(user.getFullName());
            
            double totalNormal = 0;
            double totalOt = 0;
            double totalBonus = 0;
            List<DailyWorkingHoursDto> dailyHours = new ArrayList<>();
            
            // Group by date and calculate totals
            Map<LocalDate, Double> dateHoursMap = new HashMap<>();
            for (TimesheetOld ts : userTimesheets) {
                String typeValue = ts.getType() != null ? ts.getType().getValue().toLowerCase() : "";
                double hours = ts.getHours() != null ? ts.getHours() : 0;
                
                if (typeValue.contains("normal")) {
                    totalNormal += hours;
                } else if (typeValue.contains("ot") || typeValue.contains("overtime")) {
                    totalOt += hours;
                } else if (typeValue.contains("bonus")) {
                    totalBonus += hours;
                }
                
                dateHoursMap.merge(ts.getDate(), hours, Double::sum);
            }
            
            for (Map.Entry<LocalDate, Double> dateEntry : dateHoursMap.entrySet()) {
                DailyWorkingHoursDto dailyDto = new DailyWorkingHoursDto();
                dailyDto.setDate(dateEntry.getKey());
                dailyDto.setHour(dateEntry.getValue());
                dailyHours.add(dailyDto);
            }
            
            employeeDto.setTotalNormalHours(totalNormal);
            employeeDto.setTotalOvertimeHours(totalOt);
            employeeDto.setTotalBonusHours(totalBonus);
            employeeDto.setDailyWorkingHourDtos(dailyHours);
            
            employees.add(employeeDto);
        }
        
        reportDto.setEmployees(employees);
        
        return exportService.exportToExcel(List.of(reportDto), yearMonth);
    }

    @Override
    public Map<String, Object> approvalTimesheetByPM(List<String> ids) {
        Config approvedStatus = configRepository.findByKeyAndValue("STATUS_TIMESHEET", "Accepted")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Approved status config not found"));
        
        int matchedCount = 0;
        int modifiedCount = 0;
        
        for (String id : ids) {
            TimesheetOld timesheet = timesheetRepository.findById(id).orElse(null);
            if (timesheet != null) {
                matchedCount++;
                timesheet.setStatus(approvedStatus);
                timesheetRepository.save(timesheet);
                modifiedCount++;
            }
        }
        
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("acknowledged", true);
        result.put("modifiedCount", modifiedCount);
        result.put("matchedCount", matchedCount);
        
        return result;
    }

    @Override
    public Map<String, Object> rejectTimesheetByPM(List<String> ids) {
        Config rejectedStatus = configRepository.findByKeyAndValue("STATUS_TIMESHEET", "Rejected")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rejected status config not found"));
        
        int matchedCount = 0;
        int modifiedCount = 0;
        
        for (String id : ids) {
            TimesheetOld timesheet = timesheetRepository.findById(id).orElse(null);
            if (timesheet != null) {
                matchedCount++;
                timesheet.setStatus(rejectedStatus);
                timesheetRepository.save(timesheet);
                modifiedCount++;
            }
        }
        
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("acknowledged", true);
        result.put("modifiedCount", modifiedCount);
        result.put("matchedCount", matchedCount);
        
        return result;
    }
}