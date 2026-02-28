package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.TimesheetDto.CreateTaskDto;
import com.vatek.hrmtool.dto.TimesheetDto.ExportTimesheetReportDto;
import com.vatek.hrmtool.dto.TimesheetDto.GetTimesheetByDateDto;
import com.vatek.hrmtool.dto.TimesheetDto.GetTimesheetWeeklyDto;
import com.vatek.hrmtool.dto.TimesheetDto.NewApprovalTimesheetDto;
import com.vatek.hrmtool.dto.TimesheetDto.TimesheetResponseDto;
import com.vatek.hrmtool.service.serviceImpl.UserOldPrinciple;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TimesheetService {
    TimesheetResponseDto createTimesheet(CreateTaskDto dto, String userId);
    
    TimesheetResponseDto deleteTimesheet(String id);
    
    Map<String, Object> findTimesheetByWeekly(String userId, GetTimesheetWeeklyDto params);
    
    List<NewApprovalTimesheetDto> listTimesheetByMonth(UserOldPrinciple requestUser, GetTimesheetByDateDto params);
    
    ByteArrayInputStream exportTimesheetReport(ExportTimesheetReportDto params) throws IOException;
    
    Map<String, Object> approvalTimesheetByPM(List<String> ids);
    
    Map<String, Object> rejectTimesheetByPM(List<String> ids);
}