package com.vatek.hrmtool.dto.TimesheetDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ApprovalOrRejectTimesheetDto {
    @NotEmpty(message = "IDs list cannot be empty")
    private List<String> ids;
}
