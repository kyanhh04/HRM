package com.vatek.hrmtool.dto.ProjectDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vatek.hrmtool.enumeration.ProjectState;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class CreateProjectDto {
    private Long id;
    private String projectName;
    private String clientName;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;
    private ProjectState projectState;
    private Long pmId;
    private Set<Long> memberId;
}
