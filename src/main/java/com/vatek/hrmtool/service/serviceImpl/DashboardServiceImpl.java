//package com.vatek.hrmtool.service.serviceImpl;
//
//import com.vatek.hrmtool.dto.BirthDayDto;
//import com.vatek.hrmtool.dto.LeaveRequestDto.DashboardLeaveRequestDto;
//import com.vatek.hrmtool.entity.neww.LeaveRequestEntity;
//import com.vatek.hrmtool.entity.neww.UserEntity;
//import com.vatek.hrmtool.enumeration.RequestStatus;
//import com.vatek.hrmtool.respository.RequestLeaveRepository;
//import com.vatek.hrmtool.respository.UserRepository;
//import com.vatek.hrmtool.service.DashboardService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.rest.webmvc.ResourceNotFoundException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class DashboardServiceImpl implements DashboardService {
//    @Autowired
//    private RequestLeaveRepository requestLeaveRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Override
//    public List<BirthDayDto> birthDay(){
//        LocalDate today = LocalDate.now();
//        List<UserEntity> Employee = userRepository.findAll().stream()
//                .filter(u -> u.getDateOfBirth().plusDays(1).getDayOfMonth() == today.getDayOfMonth() && u.getDateOfBirth().getMonthValue() == today.getMonthValue() )
//                .toList();
//        List<BirthDayDto> birthDayDtoList = new ArrayList<>();
//        for(UserEntity u : Employee){
//            BirthDayDto birthDayDto = new BirthDayDto();
//            birthDayDto.setBirthday(u.getDateOfBirth().plusDays(1));
//            birthDayDto.setName(u.getName());
//            birthDayDtoList.add(birthDayDto);
//        }
//        return birthDayDtoList;
//    }
//    @Override
//    public List<DashboardLeaveRequestDto> employeeOutOfOffice(){
//        List<DashboardLeaveRequestDto> leaveRequestDtoList = new ArrayList<>();
//        LocalDate now = LocalDate.now();
//        List<LeaveRequestEntity> leaveRequestsOfEmployee = requestLeaveRepository.findAll();
//        for(LeaveRequestEntity leaveRequest : leaveRequestsOfEmployee){
//            if((!leaveRequest.getStartDate().plusDays(1).isAfter(now) && !now.isAfter(leaveRequest.getEndDate().plusDays(1))) && leaveRequest.getStatus() == RequestStatus.APPROVED){
//                DashboardLeaveRequestDto leaveRequestDto = new DashboardLeaveRequestDto();
//                leaveRequestDto.setId(leaveRequest.getId());
//                leaveRequestDto.setName(leaveRequest.getUser().getName());
//                leaveRequestDto.setStartDate(leaveRequest.getStartDate().plusDays(1));
//                leaveRequestDto.setEndDate(leaveRequest.getEndDate().plusDays(1));
//                leaveRequestDto.setStartTime(leaveRequest.getStartTime());
//                leaveRequestDto.setEndTime(leaveRequest.getEndTime());
//                leaveRequestDto.setLeaveHours(leaveRequest.getLeaveHours());
//                leaveRequestDtoList.add(leaveRequestDto);
//            }
//        }
//        return leaveRequestDtoList;
//    }
//    @Override
//    public List<DashboardLeaveRequestDto> leaveRequest(){
//        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long userId = userPrinciple.getId();
//        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
//        return user.getLeaveRequests().stream().filter(l -> !l.getStartDate().plusDays(1).isBefore(LocalDate.now())).map(l -> {
//            DashboardLeaveRequestDto leaveRequestDto = new DashboardLeaveRequestDto();
//            leaveRequestDto.setName(l.getUser().getName());
//            leaveRequestDto.setId(l.getId());
//            leaveRequestDto.setStartDate(l.getStartDate().plusDays(1));
//            leaveRequestDto.setEndDate(l.getEndDate().plusDays(1));
//            leaveRequestDto.setStartTime(l.getStartTime());
//            leaveRequestDto.setEndTime(l.getEndTime());
//            leaveRequestDto.setLeaveHours(l.getLeaveHours());
//            return leaveRequestDto;
//        }).toList();
//    }
//}
