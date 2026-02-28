package com.vatek.hrmtool.dto.TimesheetDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vatek.hrmtool.dto.BaseQueryDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class GetReportDto extends BaseQueryDto{
    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @NotEmpty(message = "Projects list is required")
    private List<String> projects;
}
