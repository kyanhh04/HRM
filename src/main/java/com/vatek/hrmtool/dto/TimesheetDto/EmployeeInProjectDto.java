package com.vatek.hrmtool.dto.TimesheetDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class EmployeeInProjectDto {
    private String name;
    private double totalNormalHours;
    private double totalBonusHours;
    private double totalOvertimeHours;
    private List<DailyWorkingHoursDto> dailyWorkingHourDtos;
}
