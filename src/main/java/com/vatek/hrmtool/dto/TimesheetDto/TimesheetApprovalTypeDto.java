package com.vatek.hrmtool.dto.TimesheetDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetApprovalTypeDto {
    private String id;
    private LocalDate date;
    private String details;
    private Double hours;
    private ConfigDto type;
    private ConfigDto status;
    private UserDto user;
    private ConfigDto project;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfigDto {
        private String id;
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDto {
        private String id;
        private String fullName;
        private String avatar;
    }
}
