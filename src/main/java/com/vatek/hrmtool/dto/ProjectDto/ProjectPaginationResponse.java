package com.vatek.hrmtool.dto.ProjectDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPaginationResponse {
    private List<GetListProjectDto> data;
    private long total;
    private long paginateTotal;
}

