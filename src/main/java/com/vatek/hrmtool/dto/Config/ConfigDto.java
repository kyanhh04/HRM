package com.vatek.hrmtool.dto.Config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfigDto {

    @NotBlank(message = "Key is required")
    private String key;

    @NotBlank(message = "Value is required")
    private String value;
}
