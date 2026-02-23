// package com.vatek.hrmtool.service.serviceImpl;

// import com.vatek.hrmtool.dto.LeaveRequestDto.*;
// import com.vatek.hrmtool.dto.TimesheetDto.DaysDto;
// import com.vatek.hrmtool.entity.LeaveRequestOld;
// import com.vatek.hrmtool.entity.UserOld;
// import com.vatek.hrmtool.entity.neww.LeaveRequestEntity;
// import com.vatek.hrmtool.entity.neww.UserEntity;
// import com.vatek.hrmtool.enumeration.LeaveType;
// import com.vatek.hrmtool.enumeration.RequestStatus;
// import com.vatek.hrmtool.respository.old.LeaveRequestOldRepository;
// import com.vatek.hrmtool.respository.RequestLeaveRepository;
// import com.vatek.hrmtool.respository.old.UserOldRepository;
// import com.vatek.hrmtool.respository.UserRepository;
// import com.vatek.hrmtool.service.RequestLeaveService;
// import org.springframework.beans.BeanUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.*;
// import org.springframework.data.rest.webmvc.ResourceNotFoundException;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Service;

// import java.time.DayOfWeek;
// import java.time.LocalDate;
// import java.time.LocalTime;
// import java.time.YearMonth;
// import java.time.temporal.ChronoUnit;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Collectors;

// @Service
// public class RequestLeaveServiceImpl implements RequestLeaveService {
//     @Autowired
//     private UserOldRepository userOldRepository;
//     @Autowired
//     private RequestLeaveRepository requestLeaveRepository;
//     @Autowired
//     private UserRepository userRepository;
//     @Autowired
//     private LeaveRequestOldRepository leaveRequestOldRepository;
//     @Override
//     public CreateRequestLeaveDailyDto createRequestDaily(CreateRequestLeaveDailyDto createRequestLeaveDailyDto){
//         UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//         Long userId = userPrinciple.getId();
//         UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
//         if(createRequestLeaveDailyDto.getDate().isBefore(LocalDate.now())){
//             throw new IllegalArgumentException("Ngày nghỉ không được trước ngày hiện tại");
//         }
//         if (createRequestLeaveDailyDto.getStartTime().isAfter(createRequestLeaveDailyDto.getEndTime())) {
//             throw new IllegalArgumentException("Giờ bắt  đầu phải trước giờ kết thúc.");
//         }
//         if (!overlapping(createRequestLeaveDailyDto.getStartTime(), createRequestLeaveDailyDto.getEndTime(), user.getStartTime(), user.getEndTime())) {
//             throw new IllegalArgumentException(String.format("Khung giờ làm việc của bạn là %s - %s, vui lòng nhập lại", user.getStartTime(), user.getEndTime()));
//         }
//         if(requestLeaveRepository.existsByUserIdAndTimeRange(userId, createRequestLeaveDailyDto.getDate(), createRequestLeaveDailyDto.getStartTime(), createRequestLeaveDailyDto.getEndTime())){
//             throw new IllegalArgumentException("Khoảng thời gian này đã có đơn nghỉ");
//         }
//         if(requestLeaveRepository.existsByUserIdAndDateRangeDaily(userId, createRequestLeaveDailyDto.getDate())){
//             throw new IllegalArgumentException("Khoảng thời gian này đã có đơn nghỉ");
//         }
//         LocalTime startTime = createRequestLeaveDailyDto.getStartTime();
//         LocalTime endTime = createRequestLeaveDailyDto.getEndTime();
//         double hour = ChronoUnit.MINUTES.between(createRequestLeaveDailyDto.getStartTime(), createRequestLeaveDailyDto.getEndTime()) / 60.0;
//         LocalTime lunchStart = LocalTime.of(12, 0);
//         LocalTime lunchEnd = LocalTime.of(13, 30);
//         if (createRequestLeaveDailyDto.getStartTime().isBefore(lunchEnd) && createRequestLeaveDailyDto.getEndTime().isAfter(lunchStart)) {
//             LocalTime overlapStart = createRequestLeaveDailyDto.getStartTime().isBefore(lunchStart) ? lunchStart : createRequestLeaveDailyDto.getStartTime();
//             LocalTime overlapEnd = createRequestLeaveDailyDto.getEndTime().isAfter(lunchEnd) ? lunchEnd : createRequestLeaveDailyDto.getEndTime();
//             double overlap = ChronoUnit.MINUTES.between(overlapStart, overlapEnd) / 60.0;
//             hour -= overlap;
//         }
//         if (createRequestLeaveDailyDto.getStartTime().isBefore(lunchEnd) && createRequestLeaveDailyDto.getEndTime().isAfter(lunchStart)) {
//             if(createRequestLeaveDailyDto.getStartTime().isAfter(lunchStart)){
//                 startTime = lunchEnd;
//             }
//             if(createRequestLeaveDailyDto.getEndTime().isBefore(lunchEnd)){
//                 endTime = lunchStart;
//             }
//         }
//         LeaveRequestEntity leaveRequest = new LeaveRequestEntity();
//         BeanUtils.copyProperties(createRequestLeaveDailyDto, leaveRequest);
//         leaveRequest.setStartDate(createRequestLeaveDailyDto.getDate());
//         leaveRequest.setEndDate(createRequestLeaveDailyDto.getDate());
//         leaveRequest.setStartTime(startTime);
//         leaveRequest.setEndTime(endTime);
//         leaveRequest.setStatus(RequestStatus.REQUESTED);
//         leaveRequest.setUser(user);
//         leaveRequest.setLeaveType(LeaveType.DAILY);
//         leaveRequest.setLeaveHours(hour);
//         leaveRequest = requestLeaveRepository.save(leaveRequest);
//         BeanUtils.copyProperties(leaveRequest, createRequestLeaveDailyDto);
//         createRequestLeaveDailyDto.setUserId(user.getId());
//         return createRequestLeaveDailyDto;
//     }
//     @Override
//     public CreateRequestLeaveMultiDayDto createRequestMultiDay(CreateRequestLeaveMultiDayDto createRequestLeaveMultiDayDto){
//         UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//         Long userId = userPrinciple.getId();
//         UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
//         if(createRequestLeaveMultiDayDto.getStartDate().isAfter(createRequestLeaveMultiDayDto.getEndDate())){
//             throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kết thúc");
//         }
//         if(createRequestLeaveMultiDayDto.getStartDate().isBefore(LocalDate.now())){
//             throw new IllegalArgumentException("Ngày nghỉ không được trước ngày hiện tại");
//         }
//         if(requestLeaveRepository.existsByUserIdAndDateRange(userId, createRequestLeaveMultiDayDto.getStartDate(), createRequestLeaveMultiDayDto.getEndDate())){
//                 throw new IllegalArgumentException("Khoảng thời gian này đã có đơn nghỉ");
//         }
//         long days = ChronoUnit.DAYS.between(createRequestLeaveMultiDayDto.getStartDate(), createRequestLeaveMultiDayDto.getEndDate()) + 1;
//         double hour = days * 8.0;
//         LeaveRequestEntity leaveRequest = new LeaveRequestEntity();
//         BeanUtils.copyProperties(createRequestLeaveMultiDayDto, leaveRequest);
//         leaveRequest.setStartDate(createRequestLeaveMultiDayDto.getStartDate());
//         leaveRequest.setEndDate(createRequestLeaveMultiDayDto.getEndDate());
//         leaveRequest.setStartTime((user.getStartTime()));
//         leaveRequest.setEndTime(user.getEndTime());
//         leaveRequest.setStatus(RequestStatus.REQUESTED);
//         leaveRequest.setUser(user);
//         leaveRequest.setLeaveType(LeaveType.MULTI);
//         leaveRequest.setLeaveHours(hour);
//         leaveRequest = requestLeaveRepository.save(leaveRequest);
//         BeanUtils.copyProperties(leaveRequest, createRequestLeaveMultiDayDto);
//         createRequestLeaveMultiDayDto.setUserId(user.getId());
//         return createRequestLeaveMultiDayDto;
//     }
//     public boolean overlapping(LocalTime startLeave, LocalTime endLeave, LocalTime startTime, LocalTime endTime){
//         return !startLeave.isBefore(startTime) && !endLeave.isAfter(endTime);
//     }
//     @Override
//     public CalendarDto getListRequestLeave(YearMonth yearMonth){
//         LocalDate firstDayOfMonth = yearMonth.atDay(1);
//         int daysInMonth = yearMonth.lengthOfMonth();
//         DayOfWeek firstWeekDay = firstDayOfMonth.getDayOfWeek();
//         LocalDate today = LocalDate.now();
//         List<DaysDto> days = new ArrayList<>();
//         int prevDays = firstWeekDay.getValue() % 7;
//         LocalDate prevMonthlastDay = firstDayOfMonth.minusDays(prevDays);
//         for(int i=0;i<prevDays;i++){
//             LocalDate d = prevMonthlastDay.plusDays(i);
//             DaysDto day = new DaysDto();
//             day.setDate(d);
//             day.setInCurrentMonth(false);
//             day.setToday(false);
//             day.setLeaves(List.of());
//             days.add(day);
//         }
//         for(int i=1;i<=daysInMonth;i++){
//             LocalDate d = yearMonth.atDay(i);
//             DaysDto day = new DaysDto();
//             day.setDate(d);
//             day.setInCurrentMonth(true);
//             day.setToday(today.equals(d));
//             day.setLeaves(getLeaves(d));
//             days.add(day);
//         }
//         int totalDayAndSpillOver = prevDays + daysInMonth;
//         int trailingDays = (7 - (totalDayAndSpillOver % 7)) % 7;
//         LocalDate nextStart = yearMonth.atEndOfMonth().plusDays(1);
//         for(int i=0;i<trailingDays;i++){
//             LocalDate d = nextStart.plusDays(i);
//             DaysDto day = new DaysDto();
//             day.setToday(false);
//             day.setInCurrentMonth(false);
//             day.setLeaves(List.of());
//             day.setDate(d);
//             days.add(day);
//         }
//         CalendarDto calendarDto = new CalendarDto();
//         calendarDto.setDays(days);
//         calendarDto.setYear(yearMonth.getYear());
//         calendarDto.setMonth(yearMonth.getMonthValue());
//         calendarDto.setPrevMonth(yearMonth.minusMonths(1));
//         calendarDto.setNextMonth(yearMonth.plusMonths(1));
//         return calendarDto;
//     }
//     public List<LeaveRequestDto> getLeaves(LocalDate date){
//         List<LeaveRequestEntity> requests = requestLeaveRepository.findByDate(date);
//         return requests.stream().map(r -> {
//             LeaveRequestDto leaveDto = new LeaveRequestDto();
//             leaveDto.setId(r.getId());
//             leaveDto.setName(r.getUser().getName());
//             leaveDto.setStatus(r.getStatus());
//             leaveDto.setStartDate(r.getStartDate());
//             leaveDto.setEndDate(r.getEndDate());
//             leaveDto.setTotalHour(r.getLeaveHours());
//             if(!r.getStartDate().equals(r.getEndDate())){
//                 leaveDto.setTotalDays(ChronoUnit.DAYS.between(r.getStartDate(),r.getEndDate())+1);
//             }
//             else{
//                 leaveDto.setTotalDays(null);
//             }
//             return leaveDto;
//         }).collect(Collectors.toList());
//     }

//     @Override
//     public Page<LeaveRequestDto> getEmployeeLeaveRequests(int page, int size, Sort.Direction direction, String sortBy) {
//         Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
//         Page<LeaveRequestEntity> leaveRequests = requestLeaveRepository.findByStatusAndEmployeeRoles(RequestStatus.REQUESTED, pageable);
//         List<LeaveRequestDto> leaveRequestDtos = leaveRequests.stream().
//                 map(r -> {
//                     LeaveRequestDto leaveDto = new LeaveRequestDto();
//                     leaveDto.setId(r.getId());
//                     leaveDto.setName(r.getUser().getName());
//                     leaveDto.setStatus(r.getStatus());
//                     leaveDto.setStartDate(r.getStartDate());
//                     leaveDto.setEndDate(r.getEndDate());
//                     if (!r.getStartDate().equals(r.getEndDate())) {
//                         leaveDto.setTotalDays(ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate()) + 1);
//                     }
//                     else {
//                         leaveDto.setTotalDays(1L);
//                     }
//                     leaveDto.setTotalHour(r.getLeaveHours());
//                     return leaveDto;
//                 }).toList();
//         return new PageImpl<>(leaveRequestDtos, pageable, leaveRequests.getTotalElements());
//     }

//     @Override
//     public void approveLeaveRequest(ApproveLeaveRequest approveLeaveRequest) {
//         if(approveLeaveRequest.getIds() == null || approveLeaveRequest.getIds().isEmpty()){
//             throw new IllegalArgumentException("Danh sách ID không được rỗng");
//         }
//         for(String idStr : approveLeaveRequest.getIds()){
//             Long id = Long.parseLong(idStr);
//             LeaveRequestOld leaveRequestOld = leaveRequestOldRepository.findById(id)
//                     .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy yêu cầu nghỉ phép với ID: " + id));
// //            leaveRequestOld.setStatus(RequestStatus.APPROVED);
//             leaveRequestOldRepository.save(leaveRequestOld);
//         }
//     }

//     @Override
//     public void rejectLeaveRequest(RejectLeaveRequest rejectLeaveRequest) {
//         if(rejectLeaveRequest.getIds() == null || rejectLeaveRequest.getIds().isEmpty()){
//             throw new IllegalArgumentException("Danh sách ID không được rỗng");
//         }
//         for(String idStr : rejectLeaveRequest.getIds()){
//             Long id = Long.parseLong(idStr);
//             LeaveRequestOld leaveRequestOld = leaveRequestOldRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy yêu cầu nghỉ phép với ID: " + id));
// //            leaveRequestOld.setState(RequestStatus.REJECTED);
//             leaveRequestOldRepository.save(leaveRequestOld);
//         }
//     }

//     @Override
//     public LeaveRequestOldDto getLeaveRequestDetails(Long id) {
//         LeaveRequestOld leaveRequest = leaveRequestOldRepository.findById(id)
//             .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy yêu cầu nghỉ phép"));
//         return convertToLeaveRequestDto(leaveRequest);
//     }

//     private LeaveRequestOldDto convertToLeaveRequestDto(LeaveRequestOld leaveRequest) {
//         LeaveRequestOldDto leaveRequestDetailDto = new LeaveRequestOldDto();
//         leaveRequestDetailDto.setId(leaveRequest.getId());
// //        leaveRequestDetailDto.setAfternoon(leaveRequest.isAfternoon());
// //        leaveRequestDetailDto.setMorning(leaveRequest.isMorning());
//         leaveRequestDetailDto.setFromDay(leaveRequest.getFromDay());
//         leaveRequestDetailDto.setToDay(leaveRequest.getToDay());
// //        leaveRequestDetailDto.setStatus(leaveRequest.getState());
//         leaveRequestDetailDto.setReason(leaveRequest.getReason());
//         leaveRequestDetailDto.setName(leaveRequest.getUser().getFullName());
//         return leaveRequestDetailDto;
//     }

//     @Override
//     public void removeRequest(Long id) {
//         LeaveRequestEntity leaveRequest = requestLeaveRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn xin nghỉ"));
//         requestLeaveRepository.delete(leaveRequest);
//     }

//     @Override
//     public LeaveRequestOldDto createRequest(LeaveRequestOldDto leaveRequestOldDto){
//         UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//         Long userId = userPrinciple.getId();
//         UserOld user = userOldRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
//         if(leaveRequestOldDto.getFromDay().isBefore(LocalDate.now())){
//             throw new IllegalArgumentException("Ngày bắt đầu không được trước ngày hiện tại");
//         }
//         if(leaveRequestOldDto.getFromDay().isAfter(leaveRequestOldDto.getToDay())){
//             throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kết thúc");
//         }
//         List<LeaveRequestOld> overlappingRequests = leaveRequestOldRepository.findOverlappingRequests(leaveRequestOldDto.getFromDay(), leaveRequestOldDto.getToDay(), leaveRequestOldDto.isMorning(), leaveRequestOldDto.isAfternoon());
//         if(!overlappingRequests.isEmpty()){
//             throw new IllegalArgumentException("Khoảng thời gian này đã có đơn xin nghỉ");
//         }
//         LeaveRequestOld leaveRequestOld = new LeaveRequestOld();
//         leaveRequestOld.setFromDay(leaveRequestOldDto.getFromDay());
//         leaveRequestOld.setToDay(leaveRequestOldDto.getToDay());
// //        leaveRequestOld.setMorning(leaveRequestOldDto.isMorning());
// //        leaveRequestOld.setAfternoon(leaveRequestOldDto.isAfternoon());
//         leaveRequestOld.setReason(leaveRequestOldDto.getReason());
// //        leaveRequestOld.setState(RequestStatus.REQUESTED);
//         leaveRequestOld.setUser(user);
//         leaveRequestOld = leaveRequestOldRepository.save(leaveRequestOld);
//         LeaveRequestOldDto resultDto = new LeaveRequestOldDto();
//         resultDto.setId(leaveRequestOld.getId());
// //        resultDto.setMorning(leaveRequestOld.isMorning());
// //        resultDto.setAfternoon(leaveRequestOld.isAfternoon());
//         resultDto.setFromDay(leaveRequestOld.getFromDay());
//         resultDto.setToDay(leaveRequestOld.getToDay());
//         resultDto.setReason(leaveRequestOld.getReason());
// //        resultDto.setStatus(leaveRequestOld.getState());
//         resultDto.setName(user.getFullName());
//         return resultDto;
//     }
//     @Override
//     public List<LeaveRequestOld> findAll(){
//         return leaveRequestOldRepository.findByStatusNot(RequestStatus.REJECTED)
//                 .stream()
//                 .sorted((a,b) -> b.getCreatedDate().compareTo(a.getCreatedDate()))
//                 .toList();
//     }
//     @Override
//     public List<LeaveRequestOld> findAllUserRequest(){
//         UserPrinciple user =  (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//         Long userId = user.getId();
//         return leaveRequestOldRepository.findByUserIdAndStatusNot(userId.toString())
//                 .stream().sorted((a,b) -> b.getCreatedDate().compareTo(a.getCreatedDate())).toList();
//     }
// }
