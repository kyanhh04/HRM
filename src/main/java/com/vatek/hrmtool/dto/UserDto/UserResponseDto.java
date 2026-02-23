package com.vatek.hrmtool.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String id;
    private String fullName;
    private String email;
    private LocalDate onboardingDate;
    private String phone;
    private String name;
    private LocalDate dateOfBirth;
    private UserMentorDto onboardingMentor;
    private ConfigDto level;
    private List<ConfigDto> positions;
    private ImageDto avatar;
    private String status;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserMentorDto {
        private String id;
        private String fullName;
        private String email;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfigDto {
        private String id;
        private String key;
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageDto {
        private String id;
        private String src;
    }
}
