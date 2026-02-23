package com.vatek.hrmtool.response.common;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(builderMethodName = "successBuilder")
public class SuccessRes<T> extends CommonRes {
    private T data;
}
