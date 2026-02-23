package com.vatek.hrmtool.dto.TimesheetDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vatek.hrmtool.constant.DateConstant;
import com.vatek.hrmtool.dto.LeaveRequestDto.LeaveRequestDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DaysDto {
    @JsonFormat(pattern = DateConstant.DD_MM_YYYY)
    private LocalDate date;
    private boolean inCurrentMonth;
    private boolean isToday;
    private List<LeaveRequestDto> leaves;
}
