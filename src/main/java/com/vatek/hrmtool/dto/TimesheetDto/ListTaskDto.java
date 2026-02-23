package com.vatek.hrmtool.dto.TimesheetDto;

import com.vatek.hrmtool.enumeration.TimesheetStatus;
import lombok.Data;

@Data
public class ListTaskDto {
    private Long id;
    private TimesheetStatus timesheetStatus;
    private String projectName;
    private double workingHour;
}
