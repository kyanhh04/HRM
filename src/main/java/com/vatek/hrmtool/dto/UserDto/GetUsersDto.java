package com.vatek.hrmtool.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUsersDto extends GetUserDto {
    private String level;
    private List<String> positions;
}

