package com.vatek.hrmtool.dto.TimesheetDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DailyWorkingHoursDto {
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    private double hour;
}
