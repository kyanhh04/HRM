package com.vatek.hrmtool.dto.LeaveRequestDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vatek.hrmtool.dto.TimesheetDto.DaysDto;
import lombok.Getter;
import lombok.Setter;

import java.time.YearMonth;
import java.util.List;

@Getter
@Setter
public class CalendarDto {
    private int year;
    private int month;
    private List<DaysDto> days;
    @JsonFormat(pattern = "MM/yyyy")
    private YearMonth prevMonth;
    @JsonFormat(pattern = "MM/yyyy")
    private YearMonth nextMonth;
}
