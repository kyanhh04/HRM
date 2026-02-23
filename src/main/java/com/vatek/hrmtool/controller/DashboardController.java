//package com.vatek.hrmtool.controller;
//
//import com.vatek.hrmtool.dto.BirthDayDto;
//import com.vatek.hrmtool.dto.LeaveRequestDto.DashboardLeaveRequestDto;
//import com.vatek.hrmtool.service.DashboardService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/dashboard")
//public class DashboardController {
//    @Autowired
//    private DashboardService dashboardService;
//    @GetMapping("/birthday")
//    public ResponseEntity<?> birthDay(){
//        List<BirthDayDto> birthDayDto = dashboardService.birthDay();
//        return ResponseEntity.ok(birthDayDto);
//    }
//    @GetMapping("/employee-out-of-office")
//    public ResponseEntity<?> outOfOffice(){
//        List<DashboardLeaveRequestDto> employeeOutOfOfficeDtoList = dashboardService.employeeOutOfOffice();
//        return ResponseEntity.ok(employeeOutOfOfficeDtoList);
//    }
//    @GetMapping("/get-upcoming-leave")
//    public ResponseEntity<?> requestLeave(){
//        List<DashboardLeaveRequestDto> leaveRequestDtoList = dashboardService.leaveRequest();
//        return ResponseEntity.ok(leaveRequestDtoList);
//    }
//}
