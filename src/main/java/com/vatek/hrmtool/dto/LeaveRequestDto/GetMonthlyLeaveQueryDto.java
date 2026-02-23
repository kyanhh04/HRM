package com.vatek.hrmtool.dto.LeaveRequestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMonthlyLeaveQueryDto extends GetRequestDto {
    private String status;
}
