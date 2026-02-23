package com.vatek.hrmtool.dto.UserDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    private String fullName;
    private String phone;
    private String email;
    private String citizenID;
    private String address;
    private String level;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String password;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate onboardingDate;
    private String onboardingMentor;
    private List<String> positions;
}



