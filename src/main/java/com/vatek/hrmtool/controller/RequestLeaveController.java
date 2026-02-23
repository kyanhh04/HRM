//package com.vatek.hrmtool.controller;
//
//import com.vatek.hrmtool.dto.LeaveRequestDto.*;
//import com.vatek.hrmtool.entity.LeaveRequestOld;
//import com.vatek.hrmtool.service.RequestLeaveService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.YearMonth;
//import java.util.List;
//
//@RestController
//@RequestMapping("/request")
//public class RequestLeaveController {
//    @Autowired
//    private RequestLeaveService requestLeaveService;
//    @PostMapping("/create-request/daily")
//    public ResponseEntity<?> requestLeaveDaily(@Valid @RequestBody CreateRequestLeaveDailyDto createRequestLeaveDailyDto){
//        CreateRequestLeaveDailyDto dto = requestLeaveService.createRequestDaily(createRequestLeaveDailyDto);
//        return new ResponseEntity<>(dto ,HttpStatus.CREATED);
//    }
//    @PostMapping("/create-request/multiday")
//    public ResponseEntity<?> requestLeaveMultiday(@Valid @RequestBody CreateRequestLeaveMultiDayDto createRequestLeaveMultiDayDto){
//        CreateRequestLeaveMultiDayDto dto = requestLeaveService.createRequestMultiDay(createRequestLeaveMultiDayDto);
//        return new ResponseEntity<>(dto ,HttpStatus.CREATED);
//    }
//    @PostMapping("/create-request")
//    public ResponseEntity<?> createRequest(@Valid @RequestBody LeaveRequestOldDto leaveRequestDto){
//        LeaveRequestOldDto dto = requestLeaveService.createRequest(leaveRequestDto);
//        return ResponseEntity.ok(dto);
//    }
////    @GetMapping("/get-all-request")
////    public ResponseEntity<?> getAllRequest(
////            @RequestParam(required = false) String date,
////            @RequestParam(required = false) String
////    ){
////        List<LeaveRequestOld> leaveRequestOlds = requestLeaveService.findAll();
////        return ResponseEntity.ok(leaveRequestOlds);
////    }
//    @GetMapping("/get-user-request")
//    public ResponseEntity<?> getUserRequest(){
//        List<LeaveRequestOld> leaveRequestOlds = requestLeaveService.findAllUserRequest();
//        return ResponseEntity.ok(leaveRequestOlds);
//    }
////    @GetMapping("/get")
////    @GetMapping("/request/get-today-leaves")
////    public ResponseEntity<?> getTodayLeaves(){
////        requestLeaveService.getTodayLeaves();
////        return ;
////    }
//    @GetMapping("/get-users-monthly-leave")
//    public ResponseEntity<?> getListRequestLeave(@RequestParam(required = false) YearMonth yearMonth){
//        if(yearMonth == null){
//            yearMonth = YearMonth.now();
//        }
//        CalendarDto requestLeaveDtos = requestLeaveService.getListRequestLeave(yearMonth);
//        return ResponseEntity.ok(requestLeaveDtos);
//    }
//    // // Code cũ - dùng Role ADMIN và PM
//    // @PreAuthorize("hasAnyRole('ADMIN', 'PM')")
//    // Code mới - dùng Position POSITION_ADMIN và POSITION_PM
//    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM')")
//    @GetMapping("/get-members-request-leave-by-pm")
//    public ResponseEntity<Page<LeaveRequestDto>> getEmployeeLeaveRequests(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size,
//            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
//            @RequestParam(defaultValue = "id") String sortBy) {
//        Page<LeaveRequestDto> leaveRequests = requestLeaveService.getEmployeeLeaveRequests(page, size, direction, sortBy);
//        return ResponseEntity.ok(leaveRequests);
//    }
//    // // Code cũ - dùng Role ADMIN và PM
//    // @PreAuthorize("hasAnyRole('ADMIN', 'PM')")
//    // Code mới - dùng Position POSITION_ADMIN và POSITION_PM
//    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM')")
//    @PatchMapping("/approve-leave-request")
//    public ResponseEntity<?> approveLeaveRequest(@RequestBody ApproveLeaveRequest approveLeaveRequest) {
//        requestLeaveService.approveLeaveRequest(approveLeaveRequest);
//        return ResponseEntity.ok("Chấp nhận thành công");
//    }
//    // // Code cũ - dùng Role ADMIN và PM
//    // @PreAuthorize("hasAnyRole('ADMIN', 'PM')")
//    // Code mới - dùng Position POSITION_ADMIN và POSITION_PM
//    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM')")
//    @PatchMapping("/reject-leave-request")
//    public ResponseEntity<?> rejectLeaveRequest(@RequestBody RejectLeaveRequest rejectLeaveRequest) {
//        requestLeaveService.rejectLeaveRequest(rejectLeaveRequest);
//        return ResponseEntity.ok("Từ chối thành công");
//    }
//    // // Code cũ - dùng Role ADMIN và PM
//    // @PreAuthorize("hasAnyRole('ADMIN', 'PM')")
//    // Code mới - dùng Position POSITION_ADMIN và POSITION_PM
//    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM')")
//    @GetMapping("/get-request-by-id/{id}")
//    public ResponseEntity<LeaveRequestOldDto> getLeaveRequestDetails(@PathVariable Long id) {
//        LeaveRequestOldDto leaveRequest = requestLeaveService.getLeaveRequestDetails(id);
//        return ResponseEntity.ok(leaveRequest);
//    }
//    // // Code cũ - dùng Role ADMIN và PM
//    // @PreAuthorize("hasAnyRole('ADMIN', 'PM')")
//    // Code mới - dùng Position POSITION_ADMIN và POSITION_PM
//    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM')")
//    @DeleteMapping("/remove-request/{id}")
//    public ResponseEntity<?> removeRequest(@PathVariable Long id){
//        requestLeaveService.removeRequest(id);
//        return ResponseEntity.noContent().build();
//    }
//
//}
