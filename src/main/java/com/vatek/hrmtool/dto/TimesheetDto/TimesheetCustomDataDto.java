package com.vatek.hrmtool.dto.TimesheetDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetCustomDataDto {
    @JsonProperty("_id")
    private String id;
    private String date;
    private String details;
    private Double hours;
    private String type;
    private String status;
}
