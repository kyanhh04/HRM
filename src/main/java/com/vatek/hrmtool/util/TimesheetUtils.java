package com.vatek.hrmtool.util;

import com.vatek.hrmtool.dto.TimesheetDto.*;
import com.vatek.hrmtool.entity.ProjectOld;
import com.vatek.hrmtool.entity.TimesheetOld;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

public class TimesheetUtils {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String PENDING = "Pending";
    private static final String APPROVED = "Accepted";
    private static final String REJECTED = "Rejected";

    public static Map<String, String> handlePendingStatus(List<TimesheetCustomDataDto> array) {
        boolean hasPending = array.stream().anyMatch(item -> PENDING.equals(item.getStatus()));
        boolean hasApproved = array.stream().anyMatch(item -> APPROVED.equals(item.getStatus()));
        
        Map<String, String> result = new LinkedHashMap<>();
        if (hasPending) {
            result.put("status", PENDING);
        } else if (hasApproved) {
            result.put("status", APPROVED);
        } else {
            result.put("status", REJECTED);
        }
        return result;
    }

    public static Map<String, Double> handleTotalByType(
            String type,
            TimesheetApprovalTypeDto item,
            double normalHours,
            double otHours,
            double bonusHours) {
        
        double time = item.getHours() != null ? item.getHours() : 0;
        
        switch (type.toLowerCase()) {
            case "normal working hours":
            case "normal":
                normalHours += time;
                break;
            case "overtime (ot)":
            case "ot":
                otHours += time;
                break;
            case "bonus":
                bonusHours += time;
                break;
            default:
                break;
        }
        
        double total = bonusHours + otHours + normalHours;
        Map<String, Double> result = new LinkedHashMap<>();
        result.put("normal", normalHours);
        result.put("ot", otHours);
        result.put("bonus", bonusHours);
        result.put("total", total);
        return result;
    }

    public static String formatDateToString(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public static String formatDateToString(java.util.Date date) {
        return formatDateToString(new java.sql.Date(date.getTime()).toLocalDate());
    }

    public static LocalDate parseStringToDate(String dateString) {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }

    public static String getWeekDayName(LocalDate date) {
        return date.getDayOfWeek().toString();
    }

    public static String getWeekName(LocalDate date) {
        int weekOfYear = date.get(WeekFields.ISO.weekOfYear());
        return "Week " + weekOfYear;
    }

    public static String formatDateToIso(LocalDate date) {
        return date.format(ISO_DATE_FORMATTER);
    }

    public static String getDayOfWeekName(LocalDate date) {
        return date.getDayOfWeek().toString().substring(0, 1).toUpperCase() + 
               date.getDayOfWeek().toString().substring(1).toLowerCase();
    }

    public static List<DaysInMonthDto> initAllDaysInMonth(
            int year,
            int month,
            TimesheetCustomDataDto timesheetData) {
        
        List<DaysInMonthDto> dates = new ArrayList<>();
        LocalDate date = LocalDate.of(year, month, 1);
        LocalDate currentDate = LocalDate.parse(timesheetData.getDate(), ISO_DATE_FORMATTER);
        
        while (date.getMonthValue() == month) {
            LocalDate now = date;
            boolean isDateExist = currentDate.equals(now);
            
            if (isDateExist) {
                List<TimesheetCustomDataDto> childrenData = new ArrayList<>();
                TimesheetCustomDataDto child = new TimesheetCustomDataDto(
                    timesheetData.getId(),
                    timesheetData.getDate(),
                    timesheetData.getDetails(),
                    timesheetData.getHours(),
                    timesheetData.getType(),
                    timesheetData.getStatus()
                );
                childrenData.add(child);
                
                DaysInMonthDto dayData = new DaysInMonthDto();
                dayData.setDate(formatDateToIso(now));
                dayData.setName(getDayOfWeekName(now));
                dayData.setStatus(timesheetData.getStatus());

                TimesheetApprovalTypeDto tempDto = new TimesheetApprovalTypeDto();
                tempDto.setHours(timesheetData.getHours());
                tempDto.setType(null);
                
                Map<String, Double> totals = handleTotalByType(
                    timesheetData.getType(),
                    tempDto,
                    0, 0, 0
                );
                dayData.setNormal(totals.get("normal"));
                dayData.setOt(totals.get("ot"));
                dayData.setBonus(totals.get("bonus"));
                dayData.setTotal(totals.get("total"));
                dayData.setChildren(childrenData);
                
                dates.add(dayData);
            }
            
            date = date.plusDays(1);
        }
        
        return dates;
    }

    public static LocalDate handleDate(LocalDate date) {
        return date;
    }

    public static List<NewApprovalTimesheetDto> handleTimeSheets(
            List<TimesheetApprovalTypeDto> timesheetArray,
            LocalDate reportDate) {
        
        List<NewApprovalTimesheetDto> monthlyTimesheets = new ArrayList<>();
        Map<String, NewApprovalTimesheetDto> projectMap = new LinkedHashMap<>();
        
        for (TimesheetApprovalTypeDto currentItem : timesheetArray) {
            String projectId = currentItem.getProject().getId();
            String projectName = currentItem.getProject().getValue();

            NewApprovalTimesheetDto project = projectMap.computeIfAbsent(projectId, k -> {
                NewApprovalTimesheetDto dto = new NewApprovalTimesheetDto();
                dto.setId(projectId);
                dto.setProjectName(projectName);
                dto.setChildren(new ArrayList<>());
                return dto;
            });

            String userId = currentItem.getUser().getId();
            String userName = currentItem.getUser().getFullName();
            String userAvatar = currentItem.getUser().getAvatar();
            
            TimesheetChildrenBaseDto user = project.getChildren().stream()
                    .filter(u -> userId.equals(u.getId()))
                    .findFirst()
                    .orElse(null);
            
            if (user == null) {
                user = new TimesheetChildrenBaseDto();
                user.setId(userId);
                user.setName(userName);
                user.setAvatar(userAvatar);
                user.setNormal(0.0);
                user.setOt(0.0);
                user.setBonus(0.0);
                user.setTotal(0.0);
                user.setChildren(new ArrayList<>());
                
                project.getChildren().add(user);
            }
            TimesheetCustomDataDto customData = new TimesheetCustomDataDto();
            customData.setId(currentItem.getId());
            customData.setDate(formatDateToIso(currentItem.getDate()));
            customData.setDetails(currentItem.getDetails());
            customData.setHours(currentItem.getHours());
            customData.setType(currentItem.getType().getValue());
            customData.setStatus(currentItem.getStatus().getValue());
            
            user.getChildren().addAll(initAllDaysInMonth(
                reportDate.getYear(),
                reportDate.getMonthValue(),
                customData
            ));

            Map<String, Double> totals = handleTotalByType(
                currentItem.getType().getValue(),
                currentItem,
                user.getNormal(),
                user.getOt(),
                user.getBonus()
            );
            user.setNormal(totals.get("normal"));
            user.setOt(totals.get("ot"));
            user.setBonus(totals.get("bonus"));
            user.setTotal(totals.get("total"));
        }
        
        // Calculate user status based on all their day entries
        for (NewApprovalTimesheetDto project : projectMap.values()) {
            for (TimesheetChildrenBaseDto user : project.getChildren()) {
                user.setStatus(calculateUserStatus(user.getChildren()));
            }
        }
        
        return new ArrayList<>(projectMap.values());
    }

    private static String calculateUserStatus(List<DaysInMonthDto> days) {
        if (days == null || days.isEmpty()) {
            return PENDING;
        }
        boolean hasPending = days.stream().anyMatch(d -> PENDING.equals(d.getStatus()));
        boolean hasApproved = days.stream().anyMatch(d -> APPROVED.equals(d.getStatus()));
        
        if (hasPending) {
            return PENDING;
        } else if (hasApproved) {
            return APPROVED;
        } else {
            return REJECTED;
        }
    }

    public static List<DaysInMonthDto> initAllDaysInMonthForReports(
            int year,
            int month,
            TimesheetCustomDataDto timesheetData) {
        
        List<DaysInMonthDto> dates = new ArrayList<>();
        LocalDate date = LocalDate.of(year, month, 1);
        
        if (timesheetData != null) {
            LocalDate currentDate = LocalDate.parse(timesheetData.getDate(), ISO_DATE_FORMATTER);
            
            while (date.getMonthValue() == month) {
                LocalDate now = date;
                boolean isDateExist = currentDate.equals(now);
                
                DaysInMonthDto dayData = new DaysInMonthDto();
                dayData.setDate(formatDateToString(now));
                dayData.setName(getDayOfWeekName(now));
                
                if (isDateExist && APPROVED.equals(timesheetData.getStatus())) {
                    TimesheetApprovalTypeDto tempDto = new TimesheetApprovalTypeDto();
                    tempDto.setHours(timesheetData.getHours());
                    
                    Map<String, Double> totals = handleTotalByType(
                        timesheetData.getType(),
                        tempDto,
                        0, 0, 0
                    );
                    dayData.setNormal(totals.get("normal"));
                    dayData.setOt(totals.get("ot"));
                    dayData.setBonus(totals.get("bonus"));
                } else {
                    dayData.setNormal(0.0);
                    dayData.setOt(0.0);
                    dayData.setBonus(0.0);
                }
                
                dates.add(dayData);
                date = date.plusDays(1);
            }
        } else {
            while (date.getMonthValue() == month) {
                LocalDate now = date;
                
                DaysInMonthDto dayData = new DaysInMonthDto();
                dayData.setDate(formatDateToString(now));
                dayData.setName(getDayOfWeekName(now));
                dayData.setNormal(0.0);
                dayData.setOt(0.0);
                dayData.setBonus(0.0);
                
                dates.add(dayData);
                date = date.plusDays(1);
            }
        }
        
        return dates;
    }

    public static List<Map<String, Object>> mapTimesheetsForExcels(
            List<TimesheetOld> timesheets,
            ProjectOld project) {
        
        Map<String, Map<String, Object>> userMap = new LinkedHashMap<>();
        
        for (TimesheetOld timesheet : timesheets) {
            String userName = timesheet.getUser().getName();
            
            userMap.computeIfAbsent(userName, k -> {
                Map<String, Object> user = new LinkedHashMap<>();
                user.put("userName", userName);
                user.put("projectName", project.getProjectName());
                user.put("normalWorkingHours", new ArrayList<>());
                user.put("ot", new ArrayList<>());
                user.put("bonus", new ArrayList<>());
                return user;
            });
            
            Map<String, Object> userData = userMap.get(userName);
            
            Map<String, Object> timesheetEntry = new LinkedHashMap<>();
            timesheetEntry.put("id", timesheet.getId());
            timesheetEntry.put("date", formatDateToString(timesheet.getDate()));
            timesheetEntry.put("hours", timesheet.getHours());
            timesheetEntry.put("taskDescription", timesheet.getDetails());
            
            String typeValue = timesheet.getType().getValue().toLowerCase();
            if (typeValue.contains("normal")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> normalList = 
                    (List<Map<String, Object>>) userData.get("normalWorkingHours");
                normalList.add(timesheetEntry);
            } else if (typeValue.contains("ot")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> otList = (List<Map<String, Object>>) userData.get("ot");
                otList.add(timesheetEntry);
            } else if (typeValue.contains("bonus")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> bonusList = (List<Map<String, Object>>) userData.get("bonus");
                bonusList.add(timesheetEntry);
            }
        }
        
        return new ArrayList<>(userMap.values());
    }
}

