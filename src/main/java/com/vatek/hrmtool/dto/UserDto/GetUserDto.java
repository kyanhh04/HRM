package com.vatek.hrmtool.dto.UserDto;

import com.vatek.hrmtool.dto.BaseQueryDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserDto extends BaseQueryDto {
    @NotBlank(message = "Name is optional but if provided cannot be blank")
    private String name;
}
