//package com.vatek.hrmtool.service;
//
//import com.vatek.hrmtool.dto.LeaveRequestDto.*;
//import com.vatek.hrmtool.entity.LeaveRequestOld;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Sort;
//
//import java.time.YearMonth;
//import java.util.List;
//
//public interface RequestLeaveService {
//    CreateRequestLeaveDailyDto createRequestDaily(CreateRequestLeaveDailyDto createRequestLeaveDailyDto);
//    CreateRequestLeaveMultiDayDto createRequestMultiDay(CreateRequestLeaveMultiDayDto createRequestLeaveDailyDto);
//    CalendarDto getListRequestLeave(YearMonth yearMonth);
//    Page<LeaveRequestDto> getEmployeeLeaveRequests(int page, int size, Sort.Direction direction, String sortBy);
//    void approveLeaveRequest(ApproveLeaveRequest id);
//    void rejectLeaveRequest(RejectLeaveRequest id);
//    LeaveRequestOldDto getLeaveRequestDetails(Long id);
//    void removeRequest(Long id);
//    LeaveRequestOldDto createRequest(LeaveRequestOldDto leaveRequestDto);
//    List<LeaveRequestOld> findAll();
//    List<LeaveRequestOld> findAllUserRequest();
//}
