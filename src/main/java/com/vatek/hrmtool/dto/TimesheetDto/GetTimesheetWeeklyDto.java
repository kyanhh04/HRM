package com.vatek.hrmtool.dto.TimesheetDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetTimesheetWeeklyDto {
    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;
}
