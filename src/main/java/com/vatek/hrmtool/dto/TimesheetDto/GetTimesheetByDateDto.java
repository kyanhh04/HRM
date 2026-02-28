package com.vatek.hrmtool.dto.TimesheetDto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTimesheetByDateDto {
    @NotNull(message = "Date is required")
    private LocalDate date;

    private String workingType;

    private String status;
}
