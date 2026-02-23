package com.vatek.hrmtool.dto.LeaveRequestDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequestDto {
    @NotNull(message = "isMorning is required")
    private Boolean isMorning;

    @NotNull(message = "isAfternoon is required")
    private Boolean isAfternoon;

    @NotNull(message = "fromDay is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Start date of leave, format: YYYY-MM-DD", example = "2026-02-15")
    private LocalDate fromDay;

    @NotNull(message = "toDay is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "End date of leave, format: YYYY-MM-DD", example = "2026-02-15")
    private LocalDate toDay;

    @NotBlank(message = "reason is required")
    @Schema(description = "Reason for leave", example = "Medical appointment")
    private String reason;
}
