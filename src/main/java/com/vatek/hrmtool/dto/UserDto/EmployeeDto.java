package com.vatek.hrmtool.dto.UserDto;

import com.fasterxml.jackson.annotation.JsonFormat;
//import com.vatek.hrmtool.enumeration.Position;
//import com.vatek.hrmtool.enumeration.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalTime;
import java.util.*;

@Data
public class EmployeeDto {
    @NotBlank
    private String name;
    private String username;
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\\\\\]^_{|}~`]).{8,}$", message = "Mật khẩu phải có ít nhất 8 ký tự gồm 1 chữ hoa, chữ thường, số và ký tự đặc biệt")
    private String password;
    private String identityCard;
    @NotBlank
    private String phoneNumber1;
    @NotBlank
    private String currentAddress;
    @NotBlank
    private String permanentAddress;
    @NotBlank
    private String level;
    @NotBlank
    private String programLanguage;
    @NotBlank
    private String position;
    // // Code cũ - dùng Role
    // private Set<Role> roles;
    // Code mới - dùng Position
//    private Set<Position> positions;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
}
