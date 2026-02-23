package com.vatek.hrmtool.dto.ProjectDto;

import com.vatek.hrmtool.dto.TimesheetDto.EmployeesAndSumHourDto;
import lombok.Data;

import java.util.List;

@Data
public class ApprovalProjectDto {
    private String projectName;
    private List<EmployeesAndSumHourDto> employees;
}
