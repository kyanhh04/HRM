package com.vatek.hrmtool.dto.TimesheetDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportDto {
    String projectName;
    List<EmployeeInProjectDto> employees;
}
