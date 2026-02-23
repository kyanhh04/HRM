package com.vatek.hrmtool.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPaginationResponse {
    private List<UserResponseDto> data;
    private Long total;
    private Long paginateTotal;
}
