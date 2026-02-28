package com.vatek.hrmtool.dto.TimesheetDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetResponseDto {
    private String id;
    private String date;
    private Double hours;
    private String details;
    private String user;
    private String project;
    private String status;
    private String type;
    private String createdAt;
    private String updatedAt;
}
