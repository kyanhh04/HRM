package com.vatek.hrmtool.service.serviceImpl;

import com.vatek.hrmtool.dto.LeaveRequestDto.CreateRequestDto;
import com.vatek.hrmtool.dto.LeaveRequestDto.GetMonthlyLeaveQueryDto;
import com.vatek.hrmtool.dto.LeaveRequestDto.GetRequestDto;
import com.vatek.hrmtool.entity.Config;
import com.vatek.hrmtool.entity.LeaveRequestOld;
import com.vatek.hrmtool.entity.ProjectOld;
import com.vatek.hrmtool.entity.UserOld;
import com.vatek.hrmtool.enumeration.RequestNotificationType;
import com.vatek.hrmtool.notifications.MailNotificationsParam;
import com.vatek.hrmtool.respository.old.ConfigRepository;
import com.vatek.hrmtool.respository.old.LeaveRequestOldRepository;
import com.vatek.hrmtool.respository.old.ProjectOldRepository;
import com.vatek.hrmtool.respository.old.UserOldRepository;
import com.vatek.hrmtool.service.MailService;
import com.vatek.hrmtool.service.RequestService;
import com.vatek.hrmtool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private LeaveRequestOldRepository leaveRequestOldRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ProjectOldRepository projectOldRepository;

    @Autowired
    private UserOldRepository userOldRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private NotificationsService notificationsService;

    @Autowired
    private UserService userService;

    @Override
    public LeaveRequestOld create(CreateRequestDto createRequestDto, String userId) {
        Config pendingType = configRepository.findByKeyAndValue("STATUS", "PENDING")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PENDING status not found"));

        Config rejectType = configRepository.findByKeyAndValue("STATUS", "REJECTED")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "REJECTED status not found"));

        LeaveRequestOld leaveRequest = new LeaveRequestOld();
        leaveRequest.setFromDay(createRequestDto.getFromDay());
        leaveRequest.setToDay(createRequestDto.getToDay());
        leaveRequest.setReason(createRequestDto.getReason());
        leaveRequest.setIsMorning(createRequestDto.getIsMorning());
        leaveRequest.setIsAfternoon(createRequestDto.getIsAfternoon());
        leaveRequest.setStatus(pendingType);
        leaveRequest.setCreatedDate(LocalDateTime.now());

        UserOld user = userOldRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        leaveRequest.setUser(user);

        boolean hasOverlap = hasOverlappingRequest(createRequestDto, userId, rejectType.getId());
        if (hasOverlap) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leave time interval already exists");
        }

        List<ProjectOld> userProjects = projectOldRepository.findAll().stream()
                .filter(p -> !p.getIsDeleted() && p.getMembers() != null && p.getMembers().stream()
                        .anyMatch(m -> m.getId().equals(userId)))
                .toList();

        if (userProjects.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not in any project");
        }

        return leaveRequestOldRepository.save(leaveRequest);
    }

    private boolean hasOverlappingRequest(CreateRequestDto request, String userId, String rejectTypeId) {
        return leaveRequestOldRepository.findAll().stream()
            .filter(lr -> lr.getUser().getId().equals(userId))
            .filter(lr -> !lr.getStatus().getId().equals(rejectTypeId))
            .anyMatch(lr -> {
                boolean dateOverlap = !lr.getFromDay().isAfter(request.getToDay()) &&
                                     !lr.getToDay().isBefore(request.getFromDay());
                boolean timeOverlap = (lr.getIsMorning() && request.getIsMorning()) ||
                                     (lr.getIsAfternoon() && request.getIsAfternoon());
                return dateOverlap && timeOverlap;
            });
    }

    @Override
    public List<LeaveRequestOld> findAll(GetMonthlyLeaveQueryDto params) {
        Config rejectedType = configRepository.findByKeyAndValue("STATUS", "REJECTED")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "REJECTED status not found"));

        List<LeaveRequestOld> allRequests = leaveRequestOldRepository.findAll().stream()
            .filter(lr -> !lr.getStatus().getId().equals(rejectedType.getId()))
            .collect(Collectors.toList());

        if (params.getDate() != null && !params.getDate().isEmpty()) {
            LocalDate dateParam = LocalDate.parse(params.getDate());
            int month = dateParam.getMonthValue();
            int year = dateParam.getYear();

            allRequests = allRequests.stream()
                .filter(lr -> {
                    boolean fromDayMatch = lr.getFromDay().getMonthValue() == month && lr.getFromDay().getYear() == year;
                    boolean toDayMatch = lr.getToDay().getMonthValue() == month && lr.getToDay().getYear() == year;
                    return fromDayMatch || toDayMatch;
                })
                .collect(Collectors.toList());
        }

        int offset = params.getOffset() != null ? params.getOffset() : 0;
        int limit = params.getLimit() != null ? params.getLimit() : 10;
        int skip = offset * limit;
        int end = Math.min(skip + limit, allRequests.size());

        if (skip < allRequests.size()) {
            return allRequests.subList(skip, end);
        }
        return List.of();
    }
    @Override
    public List<LeaveRequestOld> findAllUserRequest(String userId, GetMonthlyLeaveQueryDto params) {
        Config rejectedType = configRepository.findByKeyAndValue("STATUS", "REJECTED")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "REJECTED status not found"));

        List<LeaveRequestOld> userRequests = leaveRequestOldRepository.findAll().stream()
            .filter(lr -> lr.getUser().getId().equals(userId))
            .collect(Collectors.toList());

        if (params.getStatus() != null && !params.getStatus().isEmpty()) {
            Config statusType = configRepository.findByKeyAndValue("STATUS", params.getStatus())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found"));

            userRequests = userRequests.stream()
                .filter(lr -> lr.getStatus().getId().equals(statusType.getId()))
                .collect(Collectors.toList());
        } else {
            userRequests = userRequests.stream()
                .filter(lr -> !lr.getStatus().getId().equals(rejectedType.getId()))
                .collect(Collectors.toList());
        }

        if (params.getDate() != null && !params.getDate().isEmpty()) {
            LocalDate dateParam = LocalDate.parse(params.getDate());
            int month = dateParam.getMonthValue();
            int year = dateParam.getYear();

            userRequests = userRequests.stream()
                .filter(lr -> {
                    boolean fromDayMatch = lr.getFromDay().getMonthValue() == month && lr.getFromDay().getYear() == year;
                    boolean toDayMatch = lr.getToDay().getMonthValue() == month && lr.getToDay().getYear() == year;
                    return fromDayMatch || toDayMatch;
                })
                .collect(Collectors.toList());
        }

        int offset = params.getOffset() != null ? params.getOffset() : 0;
        int limit = params.getLimit() != null ? params.getLimit() : 10;
        int skip = limit * offset;
        int end = Math.min(skip + limit, userRequests.size());

        if (skip < userRequests.size()) {
            return userRequests.subList(skip, end);
        }
        return List.of();
    }

    @Override
    public Map<String, Object> findAllUserRequestInDashboard(String userId, GetRequestDto params) {
        Config pendingType = configRepository.findByKeyAndValue("STATUS", "PENDING")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PENDING status not found"));

        int limit = params.getLimit() != null ? params.getLimit() : 10;
        int offset = params.getOffset() != null ? params.getOffset() : 0;
        int skip = limit * offset;

        List<LeaveRequestOld> allMatching = leaveRequestOldRepository.findAll().stream()
            .filter(lr -> lr.getUser().getId().equals(userId))
            .filter(lr -> lr.getStatus().getId().equals(pendingType.getId()))
            .collect(Collectors.toList());

        long total = allMatching.size();
        int end = Math.min(skip + limit, allMatching.size());
        List<LeaveRequestOld> data = skip < allMatching.size() ? allMatching.subList(skip, end) : List.of();

        Map<String, Object> result = new HashMap<>();
        result.put("data", data);
        result.put("total", total);
        return result;
    }

    @Override
    public Map<String, Object> findAllMembersRequestLeaveInDashboard(String pmId, GetRequestDto params) {
        List<ProjectOld> projects = projectOldRepository.findAll().stream()
            .filter(p -> p.getProjectManager() != null && p.getProjectManager().getId().equals(pmId))
            .filter(p -> !p.getIsDeleted())
            .collect(Collectors.toList());

        Config pendingType = configRepository.findByKeyAndValue("STATUS", "PENDING")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PENDING status not found"));

        int limit = params.getLimit() != null ? params.getLimit() : 10;
        int offset = params.getOffset() != null ? params.getOffset() : 0;
        int skip = limit * offset;

        if (!projects.isEmpty()) {
            Set<String> members = new HashSet<>();
            for (ProjectOld project : projects) {
                if (project.getProjectManager() != null) {
                    members.add(project.getProjectManager().getId());
                }
                if (project.getMembers() != null) {
                    project.getMembers().forEach(m -> members.add(m.getId()));
                }
            }

            List<LeaveRequestOld> allMatching = leaveRequestOldRepository.findAll().stream()
                .filter(lr -> members.contains(lr.getUser().getId()))
                .filter(lr -> lr.getStatus().getId().equals(pendingType.getId()))
                .collect(Collectors.toList());

            long total = allMatching.size();
            int end = Math.min(skip + limit, allMatching.size());
            List<LeaveRequestOld> data = skip < allMatching.size() ? allMatching.subList(skip, end) : List.of();

            Map<String, Object> result = new HashMap<>();
            result.put("data", data);
            result.put("total", total);
            return result;
        }

        return findAllUserRequestInDashboard(pmId, params);
    }

    @Override
    public Map<String, Object> dashboardUserUpcomingLeave(String userId, GetRequestDto params) {
        Config approvedType = configRepository.findByKeyAndValue("STATUS", "APPROVED")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "APPROVED status not found"));

        int limit = params.getLimit() != null ? params.getLimit() : 10;
        int offset = params.getOffset() != null ? params.getOffset() : 0;
        int skip = limit * offset;

        LocalDate today = LocalDate.now();

        List<LeaveRequestOld> allMatching = leaveRequestOldRepository.findAll().stream()
            .filter(lr -> lr.getUser().getId().equals(userId))
            .filter(lr -> lr.getStatus().getId().equals(approvedType.getId()))
            .filter(lr -> {
                if (params.getDate() != null && !params.getDate().isEmpty()) {
                    return !lr.getFromDay().isBefore(today);
                }
                return true;
            })
            .collect(Collectors.toList());

        long total = allMatching.size();
        int end = Math.min(skip + limit, allMatching.size());
        List<LeaveRequestOld> data = skip < allMatching.size() ? allMatching.subList(skip, end) : List.of();

        Map<String, Object> result = new HashMap<>();
        result.put("data", data);
        result.put("total", total);
        return result;
    }

    @Override
    public List<LeaveRequestOld> listMembersRequestLeaveByPM(String pmId, GetMonthlyLeaveQueryDto params) {
        List<ProjectOld> projects = projectOldRepository.findAll().stream()
            .filter(p -> !p.getIsDeleted())
            .filter(p -> {
                boolean isPM = p.getProjectManager() != null && p.getProjectManager().getId().equals(pmId);
                boolean isMember = p.getMembers() != null && p.getMembers().stream()
                    .anyMatch(m -> m.getId().equals(pmId));
                return isPM || isMember;
            })
            .collect(Collectors.toList());

        Config rejectType = configRepository.findByKeyAndValue("STATUS", "REJECTED")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "REJECTED status not found"));

        int limit = params.getLimit() != null ? params.getLimit() : 0;
        int offset = params.getOffset() != null ? params.getOffset() : 0;
        int skip = limit * offset;

        if (!projects.isEmpty()) {
            Set<String> members = new HashSet<>();
            for (ProjectOld project : projects) {
                if (project.getProjectManager() != null) {
                    members.add(project.getProjectManager().getId());
                }
                if (project.getMembers() != null) {
                    project.getMembers().forEach(m -> members.add(m.getId()));
                }
            }

            LocalDate dateParam = LocalDate.parse(params.getDate());
            int month = dateParam.getMonthValue();
            int year = dateParam.getYear();

            List<LeaveRequestOld> filtered = leaveRequestOldRepository.findAll().stream()
                .filter(lr -> members.contains(lr.getUser().getId()))
                .filter(lr -> !lr.getStatus().getId().equals(rejectType.getId()))
                .filter(lr -> {
                    boolean fromDayMatch = lr.getFromDay().getMonthValue() == month && lr.getFromDay().getYear() == year;
                    boolean toDayMatch = lr.getToDay().getMonthValue() == month && lr.getToDay().getYear() == year;
                    return fromDayMatch || toDayMatch;
                })
                .collect(Collectors.toList());

            if (params.getStatus() != null && !params.getStatus().isEmpty()) {
                Config statusType = configRepository.findByKeyAndValue("STATUS", params.getStatus())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status config not found"));
                filtered = filtered.stream()
                    .filter(lr -> lr.getStatus().getId().equals(statusType.getId()))
                    .collect(Collectors.toList());
            }

            int end = Math.min(skip + limit, filtered.size());
            if (skip < filtered.size()) {
                return filtered.subList(skip, end);
            }
            return List.of();
        }

        return findAllUserRequest(pmId, params);
    }

    private List<LeaveRequestOld> findAllRequestInMonth(String userId, int month, int year, String status) {
        List<ProjectOld> projects = projectOldRepository.findAll().stream()
            .filter(p -> !p.getIsDeleted())
            .filter(p -> {
                boolean isPM = p.getProjectManager() != null && p.getProjectManager().getId().equals(userId);
                boolean isMember = p.getMembers() != null && p.getMembers().stream()
                    .anyMatch(m -> m.getId().equals(userId));
                return isPM || isMember;
            })
            .toList();

        Set<String> members = new HashSet<>();
        for (ProjectOld project : projects) {
            if (project.getProjectManager() != null) {
                members.add(project.getProjectManager().getId());
            }
            if (project.getMembers() != null) {
                project.getMembers().forEach(m -> members.add(m.getId()));
            }
        }

        Config rejectType = configRepository.findByKeyAndValue("STATUS", "REJECTED")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "REJECTED status not found"));

        List<LeaveRequestOld> filtered = leaveRequestOldRepository.findAll().stream()
            .filter(lr -> members.contains(lr.getUser().getId()))
            .filter(lr -> !lr.getStatus().getId().equals(rejectType.getId()))
            .filter(lr -> {
                boolean fromDayMatch = lr.getFromDay().getMonthValue() == month && lr.getFromDay().getYear() == year;
                boolean toDayMatch = lr.getToDay().getMonthValue() == month && lr.getToDay().getYear() == year;
                return fromDayMatch || toDayMatch;
            })
            .collect(Collectors.toList());

        if (status != null && !status.isEmpty()) {
            Config statusType = configRepository.findByKeyAndValue("STATUS", status)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status config not found"));
            filtered = filtered.stream()
                .filter(lr -> lr.getStatus().getId().equals(statusType.getId()))
                .collect(Collectors.toList());
        }

        return filtered;
    }

    @Override
    public List<Map<String, Object>> getAllUsersLeavesInMonth(String userId, String date, String status) {
        LocalDate dateParam = LocalDate.parse(date);
        int month = dateParam.getMonthValue();
        int year = dateParam.getYear();

        List<LeaveRequestOld> leaveData = findAllRequestInMonth(userId, month, year, status);

        LinkedHashMap<String, Map<String, Object>> grouped = new LinkedHashMap<>();
        for (LeaveRequestOld lr : leaveData) {
            UserOld user = lr.getUser();
            String uid = user.getId();
            if (!grouped.containsKey(uid)) {
                Map<String, Object> entry = new LinkedHashMap<>();
                entry.put("_id", uid);
                entry.put("fullName", user.getFullName());
                entry.put("avatar", user.getAvatar() != null ? user.getAvatar().getSrc() : "");
                entry.put("children", new ArrayList<>());
                grouped.put(uid, entry);
            }
            ((List<LeaveRequestOld>) grouped.get(uid).get("children")).add(lr);
        }

        return new ArrayList<>(grouped.values());
    }

    @Override
    public LeaveRequestOld findOne(String requestId) {
        return leaveRequestOldRepository.findById(requestId)
            .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
    }

    @Override
    public void remove(String userId, String leaveId) {
        LeaveRequestOld leaveRequest = leaveRequestOldRepository.findById(leaveId)
            .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
        if (!leaveRequest.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete this leave request");
        }
        Config pendingType = configRepository.findByKeyAndValue("STATUS", "PENDING")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PENDING status not found"));

        if (!leaveRequest.getStatus().getId().equals(pendingType.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can only delete pending leave requests");
        }
        leaveRequestOldRepository.deleteById(leaveId);
    }

    @Override
    public void approveLeave(String pmId, List<String> ids) {
        Config approveType = configRepository.findByKeyAndValue("STATUS", "APPROVED")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "APPROVED status not found"));

        UserOld pmUser = userOldRepository.findById(pmId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PM not found"));

        boolean canApprove = canApproveOrReject(pmId, ids);
        boolean isPM = checkIsPm(pmUser);
        
        if (!canApprove && isPM) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot approve or reject this leave request");
        }

        String templateFilePath = "approve_leave";
        RequestNotificationType notificationType = RequestNotificationType.APPROVE_LEAVE;

        onSendEmail(pmId, ids, approveType, templateFilePath, notificationType);
    }

    private boolean canApproveOrReject(String pmId, List<String> leaveIds) {
        List<ProjectOld> projects = projectOldRepository.findAll().stream()
                .filter(p -> !p.getIsDeleted() && p.getProjectManager() != null && p.getProjectManager().getId().equals(pmId))
                .toList();

        if (projects.isEmpty()) {
            return false;
        }

        Set<String> memberIds = new HashSet<>();
        for (ProjectOld project : projects) {
            if (project.getMembers() != null) {
                project.getMembers().forEach(m -> memberIds.add(m.getId()));
            }
            memberIds.add(project.getProjectManager().getId());
        }

        List<LeaveRequestOld> leaveRequests = leaveRequestOldRepository.findAllById(leaveIds);
        for (LeaveRequestOld leave : leaveRequests) {
            if (!memberIds.contains(leave.getUser().getId())) {
                return false;
            }
        }

        return true;
    }

    private boolean checkIsPm(UserOld user) {
        if (user.getPositions() == null || user.getPositions().isEmpty()) {
            return false;
        }

        boolean haspmPosition = user.getPositions().stream()
                .anyMatch(p -> "PM".equalsIgnoreCase(p.getValue()));

        boolean hasExcludedPosition = user.getPositions().stream()
                .anyMatch(p -> "ADMIN".equalsIgnoreCase(p.getValue()) || "HR".equalsIgnoreCase(p.getValue()));

        return haspmPosition && !hasExcludedPosition;
    }

    @Override
    public void rejectLeave(String pmId, List<String> ids) {
        Config rejectType = configRepository.findByKeyAndValue("STATUS", "REJECTED")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "REJECTED status not found"));

        UserOld pmUser = userOldRepository.findById(pmId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PM not found"));

        boolean canReject = canApproveOrReject(pmId, ids);
        boolean isPM = checkIsPm(pmUser);
        
        if (!canReject && isPM) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot approve or reject this leave request");
        }

        String templateFilePath = "reject_leave";
        RequestNotificationType notificationType = RequestNotificationType.REJECT_LEAVE;

        onSendEmail(pmId, ids, rejectType, templateFilePath, notificationType);
    }

    private void onSendEmail(String pmId, List<String> ids, Config type, String templateFilePath, 
                            RequestNotificationType notificationType) {
        List<LeaveRequestOld> requestTimeOffList = leaveRequestOldRepository.findAllById(ids);
        
        if (requestTimeOffList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No leave requests found");
        }

        List<Map<String, Object>> users = new ArrayList<>();
        for (LeaveRequestOld item : requestTimeOffList) {
            UserOld userData = item.getUser();
            users.add(Map.of(
                "fullName", userData.getFullName(),
                "email", userData.getEmail(),
                "toDay", item.getToDay(),
                "fromDay", item.getFromDay()
            ));
            item.setStatus(type);
            leaveRequestOldRepository.save(item);
        }

        for (Map<String, Object> user : users) {
                String fullName = (String) user.get("fullName");
                LocalDate fromDay = (LocalDate) user.get("fromDay");
                LocalDate toDay = (LocalDate) user.get("toDay");
                String email = (String) user.get("email");

                String replacedHtml = mailService.generateLeaveTemplate(fullName, fromDay, toDay, templateFilePath);
                
                MailNotificationsParam param = new MailNotificationsParam();
                param.setReceivers(new String[]{email});
                param.setFullName(fullName);
                param.setDayFrom(java.sql.Date.valueOf(fromDay));
                param.setDayTo(java.sql.Date.valueOf(toDay));
                param.setReplacedHtml(replacedHtml);
                param.setNotificationsType(notificationType);

                var mailOptions = notificationsService.handleParseMailOption(param);
                notificationsService.sendMail(mailOptions);
        }
    }

    @Override
    public Map<String, Object> getTodayLeaves(String userId) {
        LocalDate today = LocalDate.now();
        
        Config approvedType = configRepository.findByKeyAndValue("STATUS", "APPROVED")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "APPROVED status not found"));
        
        Config adminPosition = configRepository.findByKeyAndValue("POSITION", "ADMIN")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ADMIN position not found"));

        List<UserOld> adminUsers = userOldRepository.findAll().stream()
                .filter(u -> !u.getIsDeleted() && u.getPositions() != null && 
                        u.getPositions().stream().anyMatch(p -> p.getId().equals(adminPosition.getId())))
                .toList();
        
        List<String> adminUserIds = adminUsers.stream().map(UserOld::getId).toList();

        List<LeaveRequestOld> adminLeaves = leaveRequestOldRepository.findAll().stream()
                .filter(lr -> adminUserIds.contains(lr.getUser().getId()))
                .filter(lr -> lr.getStatus().getId().equals(approvedType.getId()))
                .filter(lr -> !lr.getFromDay().isAfter(today) && !lr.getToDay().isBefore(today))
                .toList();

        Set<String> teamMemberIds = getTeamMemberIds(userId);

        List<LeaveRequestOld> memberLeaves = leaveRequestOldRepository.findAll().stream()
                .filter(lr -> teamMemberIds.contains(lr.getUser().getId()))
                .filter(lr -> !adminUserIds.contains(lr.getUser().getId()))
                .filter(lr -> lr.getStatus().getId().equals(approvedType.getId()))
                .filter(lr -> !lr.getFromDay().isAfter(today) && !lr.getToDay().isBefore(today))
                .toList();

        List<LeaveRequestOld> allTodayLeaves = new ArrayList<>(adminLeaves);
        allTodayLeaves.addAll(memberLeaves);

        Map<String, Object> result = new HashMap<>();
        result.put("morning", allTodayLeaves.stream().filter(LeaveRequestOld::getIsMorning).toList());
        result.put("afternoon", allTodayLeaves.stream().filter(LeaveRequestOld::getIsAfternoon).toList());
        result.put("allDay", allTodayLeaves.stream()
                .filter(lr -> !Boolean.TRUE.equals(lr.getIsMorning()) && !Boolean.TRUE.equals(lr.getIsAfternoon()))
                .toList());
        
        return result;
    }

    private Set<String> getTeamMemberIds(String userId) {
        Set<String> memberIds = new HashSet<>();
        
        List<ProjectOld> projects = projectOldRepository.findAll().stream()
                .filter(p -> !p.getIsDeleted())
                .filter(p -> {
                    if (p.getProjectManager() != null && p.getProjectManager().getId().equals(userId)) {
                        return true;
                    }
                    return p.getMembers() != null && p.getMembers().stream()
                            .anyMatch(m -> m.getId().equals(userId));
                })
                .toList();

        for (ProjectOld project : projects) {
            memberIds.add(project.getProjectManager().getId());
            if (project.getMembers() != null) {
                project.getMembers().forEach(m -> memberIds.add(m.getId()));
            }
        }
        
        return memberIds;
    }
}
