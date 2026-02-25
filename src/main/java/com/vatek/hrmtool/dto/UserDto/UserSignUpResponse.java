package com.vatek.hrmtool.dto.UserDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpResponse {
    private String id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String citizenID;
    private String address;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate onboardingDate;
    
    private String status;
}
