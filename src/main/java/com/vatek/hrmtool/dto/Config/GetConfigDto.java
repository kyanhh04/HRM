package com.vatek.hrmtool.dto.Config;

import com.vatek.hrmtool.dto.BaseQueryDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetConfigDto extends BaseQueryDto {

    private String searchValue;
    @NotBlank(message = "searchKey is required")
    private String searchKey;

}
