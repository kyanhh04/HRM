package com.vatek.hrmtool.dto.TimesheetDto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportTimesheetReportDto {
    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Project is required")
    private String project;
}
