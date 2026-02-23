package com.vatek.hrmtool.dto.UserDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayloadForgotPassword {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
}
