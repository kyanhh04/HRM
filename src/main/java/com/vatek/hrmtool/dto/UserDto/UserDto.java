package com.vatek.hrmtool.dto.UserDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    @NotBlank(message = "Phone is required")
    private String phone;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    private String citizenID;
    private String address;
    private String levelId;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate onboardingDate;
    
    private String onboardingMentor;
    private List<String> positions;
    private String refreshToken;
}

