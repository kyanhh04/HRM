package com.vatek.hrmtool.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BaseQueryDto {
    @Min(value = 0, message = "Limit must be >= 0")
    private Integer limit;
    @Min(value = 0, message = "Offset must be >= 0")
    private Integer offset;

}
