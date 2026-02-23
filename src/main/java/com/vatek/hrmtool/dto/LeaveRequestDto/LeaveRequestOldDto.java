package com.vatek.hrmtool.dto.LeaveRequestDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vatek.hrmtool.constant.DateConstant;
import com.vatek.hrmtool.enumeration.RequestStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class LeaveRequestOldDto {
    private Long id;
    private String name;
    private boolean isMorning;
    private boolean isAfternoon;
    @JsonFormat(pattern = DateConstant.DD_MM_YYYY)
    private LocalDate fromDay;
    @JsonFormat(pattern = DateConstant.DD_MM_YYYY)
    private LocalDate toDay;
    private RequestStatus status;
    private String reason;
}