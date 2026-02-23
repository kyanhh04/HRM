package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.dto.LeaveRequestDto.CreateRequestDto;
import com.vatek.hrmtool.dto.LeaveRequestDto.GetMonthlyLeaveQueryDto;
import com.vatek.hrmtool.dto.LeaveRequestDto.GetRequestDto;
import com.vatek.hrmtool.dto.LeaveRequestDto.UpdateRequestDto;
import com.vatek.hrmtool.entity.LeaveRequestOld;
import com.vatek.hrmtool.service.RequestService;
import com.vatek.hrmtool.service.serviceImpl.UserOldPrinciple;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.elasticsearch.annotations.Similarity.Default;

@RestController
@RequestMapping("/request")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @PostMapping("/create-request")
    public ResponseEntity<?> createRequest(@RequestBody @Valid CreateRequestDto createRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserOldPrinciple userOldPrinciple = (UserOldPrinciple) authentication.getPrincipal();
        String userId = userOldPrinciple.getId();
        LeaveRequestOld leaveRequest = requestService.create(createRequestDto, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("request", leaveRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-request")
    public ResponseEntity<?> findAll(
        @RequestParam(defaultValue = "0") Integer offset,
        @RequestParam(defaultValue = "5") Integer limit,
        @RequestParam(required = false) String date,
        @RequestParam(required = false) String status) {
            GetMonthlyLeaveQueryDto params = new GetMonthlyLeaveQueryDto();
            params.setOffset(offset);
            params.setLimit(limit);
            params.setDate(date);
            params.setStatus(status);
            List<LeaveRequestOld> requests = requestService.findAll(params);
            Map<String, Object> response = new HashMap<>();
            response.put("data", requests);
            return ResponseEntity.ok(response);
    }

    @GetMapping("/get-user-request")
    public ResponseEntity<?> findAllUserRequest(
        @RequestParam(required = false) Integer offset,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) String date,
        @RequestParam(required = false) String status) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserOldPrinciple userOldPrinciple = (UserOldPrinciple) authentication.getPrincipal();
            String userId = userOldPrinciple.getId();
            GetMonthlyLeaveQueryDto params = new GetMonthlyLeaveQueryDto();
            params.setOffset(offset != null ? offset : 0);
            params.setLimit(limit != null ? limit : 10);
            params.setDate(date);
            params.setStatus(status);
            List<LeaveRequestOld> requests = requestService.findAllUserRequest(userId, params);
            Map<String, Object> response = new HashMap<>();
            response.put("data", requests);
            return ResponseEntity.ok(response);
    }

    @GetMapping("/get-user-request-in-dashboard")
    public ResponseEntity<?> findAllUserRequestInDashboard(
        @RequestParam(required = false) Integer offset,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) String date) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserOldPrinciple userOldPrinciple = (UserOldPrinciple) authentication.getPrincipal();
            String userId = userOldPrinciple.getId();
            GetRequestDto params = new GetRequestDto();
            params.setOffset(offset);
            params.setLimit(limit);
            params.setDate(date);
            return ResponseEntity.ok(requestService.findAllUserRequestInDashboard(userId, params));
    }

    @GetMapping("/dashboard-get-members-request-leave")
    public ResponseEntity<?> findAllMembersRequestLeaveInDashboard(
        @RequestParam(required = false) Integer offset,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) String date) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserOldPrinciple userOldPrinciple = (UserOldPrinciple) authentication.getPrincipal();
            String userId = userOldPrinciple.getId();
            GetRequestDto params = new GetRequestDto();
            params.setOffset(offset);
            params.setLimit(limit);
            params.setDate(date);
            return ResponseEntity.ok(requestService.findAllMembersRequestLeaveInDashboard(userId, params));
    }

    @GetMapping("/dashboard-get-upcoming-leave")
    public ResponseEntity<?> findUpcomingLeave(
        @RequestParam(required = false) Integer offset,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) String date) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserOldPrinciple userOldPrinciple = (UserOldPrinciple) authentication.getPrincipal();
            String userId = userOldPrinciple.getId();
            GetRequestDto params = new GetRequestDto();
            params.setOffset(offset);
            params.setLimit(limit);
            params.setDate(date);
            return ResponseEntity.ok(requestService.dashboardUserUpcomingLeave(userId, params));
    }

    @GetMapping("/get-members-request-leave-by-pm")
    public ResponseEntity<?> listMembersRequestLeaveByPM(
        @RequestParam(required = false) Integer offset,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) String date,
        @RequestParam(required = false) String status) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserOldPrinciple userOldPrinciple = (UserOldPrinciple) authentication.getPrincipal();
            String userId = userOldPrinciple.getId();
            GetMonthlyLeaveQueryDto params = new GetMonthlyLeaveQueryDto();
            params.setOffset(offset);
            params.setLimit(limit);
            params.setDate(date);
            params.setStatus(status);
            return ResponseEntity.ok(requestService.listMembersRequestLeaveByPM(userId, params));
    }

    @GetMapping("/get-users-monthly-leave")
    public ResponseEntity<?> getMonthDayoffCount(
        @RequestParam String date,
        @RequestParam(required = false) String status) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserOldPrinciple userOldPrinciple = (UserOldPrinciple) authentication.getPrincipal();
            String userId = userOldPrinciple.getId();
            return ResponseEntity.ok(requestService.getAllUsersLeavesInMonth(userId, date, status));
    }

    @GetMapping("/get-request-by-id/{id}")
    public ResponseEntity<?> findOne(@PathVariable String id) {
        LeaveRequestOld request = requestService.findOne(id);
        Map<String, Object> response = new HashMap<>();
        response.put("data", request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserOldPrinciple userOldPrinciple = (UserOldPrinciple) authentication.getPrincipal();
        String userId = userOldPrinciple.getId();
        requestService.remove(userId, id);
        return ResponseEntity.ok("Leave request deleted successfully");
    }

    @PatchMapping("/approve-leave-request")
    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM')")
    public ResponseEntity<?> approveLeave(@RequestBody @Valid UpdateRequestDto updateLeaveDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserOldPrinciple userOldPrinciple = (UserOldPrinciple) authentication.getPrincipal();
            String pmId = userOldPrinciple.getId();
            requestService.approveLeave(pmId, updateLeaveDto.getIds());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Leave requests approved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/reject-leave-request")
    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM')")
    public ResponseEntity<?> rejectLeave(@RequestBody @Valid UpdateRequestDto updateLeaveDto) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserOldPrinciple userOldPrinciple = (UserOldPrinciple) authentication.getPrincipal();
            String pmId = userOldPrinciple.getId();
            requestService.rejectLeave(pmId, updateLeaveDto.getIds());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Leave requests rejected successfully");
            return ResponseEntity.ok(response);
    }
        
    @GetMapping("/get-today-leaves")
    public ResponseEntity<?> getTodayLeaves() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserOldPrinciple userOldPrinciple = (UserOldPrinciple) authentication.getPrincipal();
            String userId = userOldPrinciple.getId();
            Map<String, Object> todayLeaves = requestService.getTodayLeaves(userId);
            return ResponseEntity.ok(todayLeaves);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
