//package com.vatek.hrmtool.dto.UserDto;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.vatek.hrmtool.constant.DateConstant;
//import com.vatek.hrmtool.enumeration.Position;
//import com.vatek.hrmtool.enumeration.Role;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.Set;
//
//@Getter
//@Setter
//public class UserUpdateDto {
//    @JsonFormat(pattern = DateConstant.DD_MM_YYYY)
//    private LocalDate dateOfBirth;
//    private String username;
//    private String name;
//    private String email;
//    private String password;
//    private String identityCard;
//    private String phoneNumber1;
//    private String avatarUrl;
//    private String currentAddress;
//    private String permanentAddress;
//    private String accessToken;
//    private boolean tokenStatus;
//    private boolean isEnabled;
//    private Instant modifiedTime;
//    private Long modifiedBy;
//    private String level;
//    private String programLanguage;
//    private String position;
//    // // Code cũ - dùng Role
//    // private Set<Role> roles;
//    // Code mới - dùng Position
//    private Set<Position> positions;
//    private LocalTime startTime;
//    private LocalTime endTime;
//}