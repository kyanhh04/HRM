package com.vatek.hrmtool.dto.LeaveRequestDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vatek.hrmtool.constant.DateConstant;
import com.vatek.hrmtool.enumeration.RequestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.*;

@Data
public class CreateRequestLeaveDailyDto {
    private Long id;
    private Long userId;
    @JsonFormat(pattern = DateConstant.DD_MM_YYYY)
    private LocalDate date;
    @JsonFormat(pattern = DateConstant.HH_MM)
    @NotNull(message = "please fill start time")
    private LocalTime startTime;
    @JsonFormat(pattern = DateConstant.HH_MM)
    @NotNull(message = "please fill end time")
    private LocalTime endTime;
    @NotBlank(message = "please fill your reason")
    private String reason;
    private RequestStatus status;
    private double leaveHours;
}
