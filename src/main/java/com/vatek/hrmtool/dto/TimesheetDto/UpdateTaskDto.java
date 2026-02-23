package com.vatek.hrmtool.dto.TimesheetDto;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vatek.hrmtool.enumeration.TimesheetStatus;
import com.vatek.hrmtool.enumeration.WorkingType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Data
public class UpdateTaskDto {
    private Long id;
    @NotNull(message = "please choose a project")
    private Long projectId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    @NotNull(message = "please fill your working type")
    private WorkingType workingType;
    @DecimalMin(value = "0.5", message = "Only 0,5h - 8h is acceptable")
    @Max(value = 8, message = "Only 0,5h- 8h is acceptable")
    @NotNull(message = "please fill your working hours")
    private double workingHour;
    private TimesheetStatus timesheetStatus;
    @Size(min = 10, message = "Please fill more than 10 characters")
    @NotBlank(message = "Please fill the description of your task")
    private String description;
}
