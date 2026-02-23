package com.vatek.hrmtool.notifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DateRangeInfo {
    private boolean isDate;
    private String dayFrom;
    private String dayTo;
}
