package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.LeaveRequestDto.CreateRequestDto;
import com.vatek.hrmtool.dto.LeaveRequestDto.GetRequestDto;
import com.vatek.hrmtool.dto.LeaveRequestDto.GetMonthlyLeaveQueryDto;
import com.vatek.hrmtool.entity.LeaveRequestOld;

import java.util.List;
import java.util.Map;

public interface RequestService {
    LeaveRequestOld create(CreateRequestDto createRequestDto, String userId);
    List<LeaveRequestOld> findAll(GetMonthlyLeaveQueryDto params);
    List<LeaveRequestOld> findAllUserRequest(String userId, GetMonthlyLeaveQueryDto params);
    Map<String, Object> findAllUserRequestInDashboard(String userId, GetRequestDto params);
    Map<String, Object> findAllMembersRequestLeaveInDashboard(String pmId, GetRequestDto params);
    Map<String, Object> dashboardUserUpcomingLeave(String userId, GetRequestDto params);
    List<LeaveRequestOld> listMembersRequestLeaveByPM(String pmId, GetMonthlyLeaveQueryDto params);
    List<Map<String, Object>> getAllUsersLeavesInMonth(String userId, String date, String status);
    LeaveRequestOld findOne(String requestId);
    void remove(String userId, String leaveId);
    void approveLeave(String pmId, List<String> ids);
    void rejectLeave(String pmId, List<String> ids);
    Map<String, Object> getTodayLeaves(String userId);
}
