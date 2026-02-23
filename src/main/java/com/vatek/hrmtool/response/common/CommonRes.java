package com.vatek.hrmtool.response.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder(builderMethodName = "commonBuilder")
public class























CommonRes {

    /**
     *
     */
    private String message;
    private String code;
}
