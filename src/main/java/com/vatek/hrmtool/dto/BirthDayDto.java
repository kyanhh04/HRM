package com.vatek.hrmtool.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vatek.hrmtool.constant.DateConstant;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BirthDayDto {
    private String name;
    @JsonFormat(pattern = DateConstant.DD_MM_YYYY)
    private LocalDate birthday;
}
