package com.vatek.hrmtool.dto.LeaveRequestDto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.vatek.hrmtool.dto.BaseQueryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRequestDto extends BaseQueryDto{
    @NotBlank(message = "date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String date;
}
