package com.vatek.hrmtool.dto.LeaveRequestDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestDto {
    @NotEmpty(message = "ids array is required")
    private List<String> ids;
}
