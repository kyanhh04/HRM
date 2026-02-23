package com.vatek.hrmtool.dto.LeaveRequestDto;

import lombok.Data;

import java.util.List;

@Data
public class ApproveLeaveRequest {
    List<String> ids;
}
