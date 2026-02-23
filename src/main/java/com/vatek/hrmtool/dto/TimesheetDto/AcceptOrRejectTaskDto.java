package com.vatek.hrmtool.dto.TimesheetDto;

import com.vatek.hrmtool.enumeration.TimesheetStatus;
import lombok.Data;

@Data
public class AcceptOrRejectTaskDto {
    private Long taskId;
    private TimesheetStatus timesheetStatus;
}
