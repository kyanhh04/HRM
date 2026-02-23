package com.vatek.hrmtool.dto.LeaveRequestDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vatek.hrmtool.constant.DateConstant;
import com.vatek.hrmtool.enumeration.RequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LeaveRequestDto {
    private Long id;
    private String name;
    @JsonFormat(pattern = DateConstant.DD_MM_YYYY)
    private LocalDate startDate;
    @JsonFormat(pattern = DateConstant.DD_MM_YYYY)
    private LocalDate endDate;
    private Long totalDays;
    @JsonFormat(pattern = DateConstant.DD_MM_YYYY)
    private Double totalHour;
    private RequestStatus status;
}
