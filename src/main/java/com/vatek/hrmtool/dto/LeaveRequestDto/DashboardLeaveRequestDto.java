package com.vatek.hrmtool.dto.LeaveRequestDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vatek.hrmtool.constant.DateConstant;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class DashboardLeaveRequestDto {
    private Long id;
    private String name;
    @JsonFormat(pattern = DateConstant.HH_MM)
    private LocalTime startTime;
    @JsonFormat(pattern = DateConstant.HH_MM)
    private LocalTime endTime;
    @JsonFormat(pattern = DateConstant.DD_MM_YYYY)
    private LocalDate startDate;
    @JsonFormat(pattern = DateConstant.DD_MM_YYYY)
    private LocalDate endDate;
    private Double leaveHours;
}