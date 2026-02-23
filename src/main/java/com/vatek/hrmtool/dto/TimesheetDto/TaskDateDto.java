package com.vatek.hrmtool.dto.TimesheetDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDateDto {
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    private double hour;
    private double normal;
    private double bonus;
    private double OT;
}
