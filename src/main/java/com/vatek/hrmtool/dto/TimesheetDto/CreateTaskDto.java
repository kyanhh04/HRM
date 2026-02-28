package com.vatek.hrmtool.dto.TimesheetDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTaskDto {
    @NotBlank(message = "Working type is required")
    private String type;

    private String status;

    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @NotBlank(message = "Project is required")
    private String project;

    @NotNull(message = "Hours is required")
    @DecimalMin(value = "0.5", message = "Only 0.5h - 8h is acceptable")
    @DecimalMax(value = "8", message = "Only 0.5h - 8h is acceptable")
    private Double hours;

    private String details;
}
