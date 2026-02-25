package com.vatek.hrmtool.dto.ProjectDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetListProjectDto {
    private String id;
    private String projectName;
    private UserProjectDto projectManager;
    private List<UserProjectDto> members;
    private LocalDate startDate;
    private LocalDate endDate;
    private String clientName;
    private String state;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserProjectDto {
        private String id;
        private String fullName;
        private String email;
        private ImageDto avatar;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageDto {
        private String id;
        private String src;
    }
}
