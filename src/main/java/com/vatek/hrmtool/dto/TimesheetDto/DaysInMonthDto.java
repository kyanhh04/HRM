package com.vatek.hrmtool.dto.TimesheetDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DaysInMonthDto {
    private String date;
    private String name;
    private Double normal;
    private Double ot;
    private Double bonus;
    private Double total;
    private String status;
    private List<TimesheetCustomDataDto> children;
}
