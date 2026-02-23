package com.vatek.hrmtool.dto.LeaveRequestDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vatek.hrmtool.constant.DateConstant;
import com.vatek.hrmtool.enumeration.RequestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateRequestLeaveMultiDayDto {
    private Long id;
    private Long userId;
    @JsonFormat(pattern = DateConstant.DD_MM_YYYY)
    @NotNull(message = "please fill start date")
    private LocalDate startDate;
    @JsonFormat(pattern = DateConstant.DD_MM_YYYY)
    @NotNull(message = "please fill end date")
    private LocalDate endDate;
    @NotBlank(message = "please fill your reason")
    private String reason;
    private double leaveHours;
    private RequestStatus status;
}
