package com.vatek.hrmtool.dto.TimesheetDto;

import lombok.Data;

import java.util.List;

@Data
public class EmployeesAndSumHourDto {
    private String name;
    private double sumHour;
    private double sumNormal;
    private double sumBonus;
    private double sumOT;
    private List<TaskDateDto> taskDateDtos;
}
