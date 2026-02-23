// package com.vatek.hrmtool.service.serviceImpl;

// import com.vatek.hrmtool.dto.ProjectDto.ApprovalProjectDto;
// import com.vatek.hrmtool.dto.TimesheetDto.*;
// import com.vatek.hrmtool.entity.neww.LeaveRequestEntity;
// import com.vatek.hrmtool.entity.neww.ProjectEntity;
// import com.vatek.hrmtool.entity.neww.TaskEntity;
// import com.vatek.hrmtool.entity.neww.UserEntity;
// import com.vatek.hrmtool.enumeration.ProjectState;
// import com.vatek.hrmtool.enumeration.TimesheetStatus;
// import com.vatek.hrmtool.enumeration.WorkingType;
// import com.vatek.hrmtool.respository.ProjectRepository;
// import com.vatek.hrmtool.respository.RequestLeaveRepository;
// import com.vatek.hrmtool.respository.TaskRepository;
// import com.vatek.hrmtool.respository.UserRepository;
// import com.vatek.hrmtool.service.TaskService;
// import com.vatek.hrmtool.service.serviceImpl.UserOldPrinciple;
// import org.modelmapper.ModelMapper;
// import org.springframework.beans.BeanUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.rest.webmvc.ResourceNotFoundException;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Service;

// import java.time.LocalDate;
// import java.time.YearMonth;
// import java.util.*;
// import java.util.stream.Collectors;

// @Service
// public class TaskServiceImpl implements TaskService {
//     @Autowired
//     private TaskRepository taskRepository;
//     @Autowired
//     private UserRepository userRepository;
//     @Autowired
//     private ProjectRepository projectRepository;
//     @Autowired
//     private ModelMapper modelMapper;
//     @Autowired
//     private RequestLeaveRepository requestLeaveRepository;
//     @Override
//     public CreateTaskDto createTask(CreateTaskDto createTaskDto){
//         TaskEntity taskEntity = new TaskEntity();
//         // // Code cũ - dùng UserPrinciple (dành cho UserEntity)
//         // UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//         // Code mới - dùng UserOldPrinciple (dành cho UserOld)
//         UserOldPrinciple userOldPrinciple = (UserOldPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//         Long userId = userOldPrinciple.getId();
//         UserEntity user = userRepository.findById(userId)
//                 .orElseThrow(() -> new ResourceNotFoundException("không tìm thấy người dùng"));
//         ProjectEntity project = projectRepository.findById(createTaskDto.getProjectId())
//                 .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dự án"));
//         int check = 0;
//         for(ProjectEntity projectEntity : user.getProjects()){
//             if(projectEntity.getId().equals(project.getId())){
//                 check = 1;
//             }
//         }
//         if(check==0){
//             throw new IllegalArgumentException("User không nằm trong dự án này");
//         }
//         List<TaskEntity> tasks = taskRepository.findByUserIdAndDate(userId, createTaskDto.getDate());
//         List<LeaveRequestEntity> leaveRequests = user.getLeaveRequests();
//         for(LeaveRequestEntity leaveRequest : leaveRequests){
//             if(
//                 (!createTaskDto.getDate().isBefore(leaveRequest.getStartDate().plusDays(1)) &&
//                  !createTaskDto.getDate().isAfter(leaveRequest.getEndDate().plusDays(1)))){
//                 throw new IllegalArgumentException("Không thể tạo task vào ngày đã có đơn nghỉ phép");
//             }
//         }
//         if(tasks == null){
//             tasks = Collections.emptyList();
//         }
//         double totalHours = tasks.stream().mapToDouble(TaskEntity::getWorkingHour).sum();
//         double normalWorking = tasks.stream().filter(t -> t.getWorkingType() == WorkingType.NORMAL).mapToDouble(TaskEntity::getWorkingHour).sum();
//         if(createTaskDto.getWorkingType() == WorkingType.OT_CLIENT || createTaskDto.getWorkingType() == WorkingType.OT){
//             if(normalWorking < 8){
//                 throw new IllegalArgumentException("Chưa đủ 8 tiếng normal working hour, không được log bonus/OT ");
//             }
//         }
//         if(createTaskDto.getWorkingType() == WorkingType.NORMAL){
//             if(normalWorking >= 8){
//                 throw new IllegalArgumentException("Đã đủ 8 tiếng normal working hour");
//             }
//             if(createTaskDto.getWorkingHour() + normalWorking > 8){
//                 throw new IllegalArgumentException("Tổng giờ normal không được quá 8 tiếng");
//             }
//         }
//         if(totalHours + createTaskDto.getWorkingHour() > 24){
//             throw new IllegalArgumentException("Tổng giờ làm trong ngày không được vượt quá 24 tiếng");
//         }
//         BeanUtils.copyProperties(createTaskDto, taskEntity, "taskStatus");
//         taskEntity.setProject(project);
//         taskEntity.setTimesheetStatus(TimesheetStatus.PENDING);
//         taskEntity.setUser(user);
//         taskEntity = taskRepository.save(taskEntity);
//         BeanUtils.copyProperties(taskEntity, createTaskDto);
//         return createTaskDto;
//     }
//     @Override
//     public List<ProjectOfUserDto> getProjects(Long userId){
//         UserEntity user = userRepository.findById(userId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
//         Collection<ProjectEntity> projects = user.getProjects();
//         List<ProjectOfUserDto> projectOfUserDtoList = new ArrayList<>();
//         for(ProjectEntity project : projects){
//             if(project.getProjectState() != ProjectState.DONE){
//                 ProjectOfUserDto dto = new ProjectOfUserDto();
//                 dto.setId(userId);
//                 dto.setProjectName(project.getProjectName());
//                 projectOfUserDtoList.add(dto);
//             }
//         }
//         return projectOfUserDtoList;
//     }
//     @Override
//     public UpdateTaskDto updateTask(UpdateTaskDto updateTaskDto, Long id){
//         TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy task"));
//         UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//         Long userId = userPrinciple.getId();
//         if(!taskEntity.getUser().getId().equals(userId)){
//             throw new AccessDeniedException("Bạn không có quyền cập nhật task này");
//         }
//         if(taskEntity.getTimesheetStatus() == TimesheetStatus.ACCEPTED || taskEntity.getTimesheetStatus() == TimesheetStatus.REJECTED){
//             throw new IllegalArgumentException("Task đã được xử lý, không thể thay đổi trạng thái");
//         }
//         List<TaskEntity> tasks = taskRepository.findByUserIdAndDate(userId, updateTaskDto.getDate());
//         if(tasks == null){
//             tasks = Collections.emptyList();
//         }
//         double totalHours = tasks.stream().filter(t -> !t.getId().equals(id)).mapToDouble(TaskEntity::getWorkingHour).sum();
//         double normalWorking = tasks.stream().filter(t -> !t.getId().equals(id)).filter(t -> t.getWorkingType() == WorkingType.NORMAL).mapToDouble(TaskEntity::getWorkingHour).sum();
//         if(updateTaskDto.getWorkingType() == WorkingType.NORMAL){
//             if(updateTaskDto.getWorkingHour() + normalWorking > 8){
//                 throw new IllegalArgumentException("Tổng giờ normal không được quá 8 tiếng");
//             }
//         }
//         if(taskEntity.getWorkingType() == WorkingType.NORMAL && (updateTaskDto.getWorkingType() == WorkingType.OT_CLIENT || updateTaskDto.getWorkingType() == WorkingType.OT)){
//             throw new IllegalArgumentException("Không thể chuyển từ normal working sang bonus/OT");
//         }
//         if(updateTaskDto.getWorkingType() == WorkingType.NORMAL && (taskEntity.getWorkingType() == WorkingType.OT_CLIENT || taskEntity.getWorkingType() == WorkingType.OT)){
//             throw new IllegalArgumentException("Không thể chuyển từ bonus/OT working sang normal working");
//         }
//         if(totalHours + updateTaskDto.getWorkingHour() > 24){
//             throw new IllegalArgumentException("Tổng giờ làm trong ngày không được vượt quá 24 tiếng");
//         }
//         BeanUtils.copyProperties(updateTaskDto, taskEntity, "taskStatus", "id", "date");
//         ProjectEntity project = projectRepository.findById(updateTaskDto.getProjectId())
//                 .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dự án"));
//         taskEntity.setProject(project);
//         taskEntity = taskRepository.save(taskEntity);
//         BeanUtils.copyProperties(taskEntity, updateTaskDto, "date");
//         updateTaskDto.setDate(taskEntity.getDate());
//         return updateTaskDto;
//     }
//     @Override
//     public List<ListTaskGroupDto> getListTask(LocalDate startDate, LocalDate endDate){
//         UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//         Long userId = userPrinciple.getId();
//         List<ListTaskGroupDto> result = new ArrayList<>();
//         List<TaskEntity> tasks = taskRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
//         Map<LocalDate, List<TaskEntity>> grouped = new TreeMap<>(tasks.stream().collect(Collectors.groupingBy(TaskEntity::getDate)));
//         for(Map.Entry<LocalDate, List<TaskEntity>> map : grouped.entrySet()){
//             LocalDate date  = map.getKey();
//             List<TaskEntity> task = map.getValue();
//             double totalHour = task.stream().mapToDouble(TaskEntity::getWorkingHour).sum();
//             List<ListTaskDto> listTaskDtos = task.stream().map(t -> {
//                 ListTaskDto listTaskDto = new ListTaskDto();
//                 BeanUtils.copyProperties(t, listTaskDto);
//                 listTaskDto.setProjectName(t.getProject().getProjectName());
//                 return listTaskDto;
//             }).toList();
//             ListTaskGroupDto listTaskGroupDto = new ListTaskGroupDto();
//             listTaskGroupDto.setDate(date);
//             listTaskGroupDto.setTotalHour(totalHour);
//             listTaskGroupDto.setTasks(listTaskDtos);
//             result.add(listTaskGroupDto);
//         }
//         return result;
//     }
//     @Override
//     public List<ApprovalProjectDto> getApprovalList(YearMonth date, WorkingType workingType, TimesheetStatus timesheetStatus){
//         UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//         Long userId = userPrinciple.getId();
//         UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
//         List<ProjectEntity> projects = user.getManageProjectList();
//         List<ApprovalProjectDto> approvalProjectDtoList = new ArrayList<>();
//         for(ProjectEntity project : projects){
//             String projectName = project.getProjectName();
//             ApprovalProjectDto approvalProjectDto = new ApprovalProjectDto();
//             approvalProjectDto.setProjectName(projectName);
//             List<EmployeesAndSumHourDto> employeesAndSumHourDtos = new ArrayList<>();
//             Collection<UserEntity> users = project.getUsers();
//             for(UserEntity userEntity : users){
//                 EmployeesAndSumHourDto employeesAndSumHourDto = new EmployeesAndSumHourDto();
//                 employeesAndSumHourDto.setName(userEntity.getName());
//                 double totalNormal = 0;
//                 double totalOT = 0;
//                 double totalBonus = 0;
//                 double totalHour = 0;
//                 if (workingType == WorkingType.ALL) {
//                     totalNormal = userEntity.getTasks().stream()
//                             .filter(t -> t.getProject().getId().equals(project.getId())
//                                     && t.getWorkingType() == WorkingType.NORMAL
//                                     && t.getDate().getMonthValue() == date.getMonthValue()
//                                     && t.getTimesheetStatus() == timesheetStatus)
//                             .mapToDouble(TaskEntity::getWorkingHour)
//                             .sum();

//                     totalOT = userEntity.getTasks().stream()
//                             .filter(t -> t.getProject().getId().equals(project.getId())
//                                     && t.getWorkingType() == WorkingType.OT
//                                     && t.getDate().getMonthValue() == date.getMonthValue()
//                                     && t.getTimesheetStatus() == timesheetStatus)
//                             .mapToDouble(TaskEntity::getWorkingHour)
//                             .sum();

//                     totalBonus = userEntity.getTasks().stream()
//                             .filter(t -> t.getProject().getId().equals(project.getId())
//                                     && t.getWorkingType() == WorkingType.OT_CLIENT
//                                     && t.getDate().getMonthValue() == date.getMonthValue()
//                                     && t.getTimesheetStatus() == timesheetStatus)
//                             .mapToDouble(TaskEntity::getWorkingHour)
//                             .sum();
//                     totalHour = totalNormal + totalOT + totalBonus;
//                 } else if (workingType == WorkingType.NORMAL) {
//                     totalNormal = userEntity.getTasks().stream()
//                             .filter(t -> t.getProject().getId().equals(project.getId())
//                                     && t.getWorkingType() == WorkingType.NORMAL
//                                     && t.getDate().getMonthValue() == date.getMonthValue()
//                                     && t.getTimesheetStatus() == timesheetStatus)
//                             .mapToDouble(TaskEntity::getWorkingHour)
//                             .sum();
//                     totalHour = totalNormal;
//                 } else if (workingType == WorkingType.OT) {
//                     totalOT = userEntity.getTasks().stream()
//                             .filter(t -> t.getProject().getId().equals(project.getId())
//                                     && t.getWorkingType() == WorkingType.OT
//                                     && t.getDate().getMonthValue() == date.getMonthValue()
//                                     && t.getTimesheetStatus() == timesheetStatus)
//                             .mapToDouble(TaskEntity::getWorkingHour)
//                             .sum();
//                     totalHour = totalOT;
//                 } else if (workingType == WorkingType.OT_CLIENT) {
//                     totalBonus = userEntity.getTasks().stream()
//                             .filter(t -> t.getProject().getId().equals(project.getId())
//                                     && t.getWorkingType() == WorkingType.OT_CLIENT
//                                     && t.getDate().getMonthValue() == date.getMonthValue()
//                                     && t.getTimesheetStatus() == timesheetStatus)
//                             .mapToDouble(TaskEntity::getWorkingHour)
//                             .sum();
//                     totalHour = totalBonus;
//                 }
//                 employeesAndSumHourDto.setSumHour(totalHour);
//                 employeesAndSumHourDto.setSumNormal(totalNormal);
//                 employeesAndSumHourDto.setSumOT(totalOT);
//                 employeesAndSumHourDto.setSumBonus(totalBonus);
//                 List<TaskDateDto> taskDateDtos = new ArrayList<>();
//                 LocalDate start = date.atDay(1);
//                 LocalDate end = date.atEndOfMonth();
//                 for(LocalDate i = start; !i.isAfter(end) ; i = i.plusDays(1)){
//                     if(workingType == WorkingType.ALL){
//                         List<TaskEntity> taskList = taskRepository.findByUserIdAndProjectIdAndDateAndTimesheetStatus(userEntity.getId(), project.getId(), i, timesheetStatus);
//                         if (!taskList.isEmpty()) {
//                             TaskDateDto taskDateDto = new TaskDateDto();
//                             taskDateDto.setDate(i);
//                             double hour = taskList.stream().mapToDouble(TaskEntity::getWorkingHour).sum();
//                             double normal = taskList.stream().filter(t -> t.getWorkingType() == WorkingType.NORMAL).mapToDouble(TaskEntity::getWorkingHour).sum();
//                             double OT = taskList.stream().filter(t -> t.getWorkingType() == WorkingType.OT).mapToDouble(TaskEntity::getWorkingHour).sum();
//                             double bonus = taskList.stream().filter(t -> t.getWorkingType() == WorkingType.OT_CLIENT).mapToDouble(TaskEntity::getWorkingHour).sum();
//                             taskDateDto.setHour(hour);
//                             taskDateDto.setOT(OT);
//                             taskDateDto.setNormal(normal);
//                             taskDateDto.setBonus(bonus);
//                             taskDateDtos.add(taskDateDto);
//                         }
//                     }
//                     else{
//                         List<TaskEntity> taskList = taskRepository.findByUserIdAndProjectIdAndDateAndTimesheetStatusAndWorkingType(userEntity.getId(), project.getId(), i, timesheetStatus, workingType);
//                         if (!taskList.isEmpty()) {
//                             TaskDateDto taskDateDto = new TaskDateDto();
//                             taskDateDto.setDate(i);
//                             double hour = taskList.stream().mapToDouble(TaskEntity::getWorkingHour).sum();
//                             double normal = taskList.stream().filter(t -> t.getWorkingType() == WorkingType.NORMAL).mapToDouble(TaskEntity::getWorkingHour).sum();
//                             double OT = taskList.stream().filter(t -> t.getWorkingType() == WorkingType.OT).mapToDouble(TaskEntity::getWorkingHour).sum();
//                             double bonus = taskList.stream().filter(t -> t.getWorkingType() == WorkingType.OT_CLIENT).mapToDouble(TaskEntity::getWorkingHour).sum();
//                             taskDateDto.setHour(hour);
//                             taskDateDto.setOT(OT);
//                             taskDateDto.setNormal(normal);
//                             taskDateDto.setBonus(bonus);
//                             taskDateDtos.add(taskDateDto);
//                         }
//                     }
//                 }
//                 employeesAndSumHourDto.setTaskDateDtos(taskDateDtos);
//                 employeesAndSumHourDtos.add(employeesAndSumHourDto);
//             }
//             approvalProjectDto.setEmployees(employeesAndSumHourDtos);
//             approvalProjectDtoList.add(approvalProjectDto);
//         }
//         return approvalProjectDtoList;
//     }
//     @Override
//     public void acceptTask(TimesheetStatus timesheetStatus, Long taskId){
//         TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy task"));
//         if(task.getTimesheetStatus() == TimesheetStatus.ACCEPTED || task.getTimesheetStatus() == TimesheetStatus.REJECTED){
//             throw new IllegalArgumentException("Task đã được xử lý, không thể thay đổi trạng thái");
//         }
//         task.setTimesheetStatus(timesheetStatus);
//         task.setDate(task.getDate().plusDays(1));
//         taskRepository.save(task);
//     }
//     @Override
//     public void rejectTask(TimesheetStatus timesheetStatus, Long taskId){
//         TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy task"));
//         if(task.getTimesheetStatus() == TimesheetStatus.ACCEPTED || task.getTimesheetStatus() == TimesheetStatus.REJECTED){
//             throw new IllegalArgumentException("Task đã được xử lý, không thể thay đổi trạng thái");
//         }
//         task.setDate(task.getDate().plusDays(1));
//         task.setTimesheetStatus(timesheetStatus);
//         taskRepository.save(task);
//     }
//     @Override
//     public Page<ReportDto> getReport(YearMonth yearMonth, String projectName, int page, int size){
//         List<ReportDto> reportDtoList = new ArrayList<>();
//         UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//         Long userId = userPrinciple.getId();
//         UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
    
//         List<ProjectEntity> projects = user.getManageProjectList();
//         if(!projectName.equals("ALL")){
//             projects = projects.stream()
//                     .filter(p -> p.getProjectName().equals(projectName))
//                     .collect(Collectors.toList());
//         }
//         for(ProjectEntity project : projects){
//             ReportDto reportDto = new ReportDto();
//             reportDto.setProjectName(project.getProjectName());
//             List<EmployeeInProjectDto> employeeInProjectDtos = new ArrayList<>();
//             Collection<UserEntity> users = project.getUsers();
//             for(UserEntity userEntity : users){
//                 EmployeeInProjectDto employeeInProjectDto = new EmployeeInProjectDto();
//                 employeeInProjectDto.setName(userEntity.getName());
//                 double totalNormal = userEntity.getTasks().stream()
//                         .filter(t -> t.getProject().getId().equals(project.getId())
//                                 && t.getWorkingType() == WorkingType.NORMAL
//                                 && t.getDate().getMonthValue() == yearMonth.getMonthValue()
//                                 && t.getDate().getYear() == yearMonth.getYear()
//                                 && t.getTimesheetStatus() == TimesheetStatus.ACCEPTED)
//                         .mapToDouble(TaskEntity::getWorkingHour)
//                         .sum();
//                 double totalOT = userEntity.getTasks().stream()
//                         .filter(t -> t.getProject().getId().equals(project.getId())
//                                 && t.getWorkingType() == WorkingType.OT
//                                 && t.getDate().getMonthValue() == yearMonth.getMonthValue()
//                                 && t.getDate().getYear() == yearMonth.getYear()
//                                 && t.getTimesheetStatus() == TimesheetStatus.ACCEPTED)
//                         .mapToDouble(TaskEntity::getWorkingHour)
//                         .sum();
//                 double totalBonus = userEntity.getTasks().stream()
//                         .filter(t -> t.getProject().getId().equals(project.getId())
//                                 && t.getWorkingType() == WorkingType.OT_CLIENT
//                                 && t.getDate().getMonthValue() == yearMonth.getMonthValue()
//                                 && t.getDate().getYear() == yearMonth.getYear()
//                                 && t.getTimesheetStatus() == TimesheetStatus.ACCEPTED)
//                         .mapToDouble(TaskEntity::getWorkingHour)
//                         .sum();
//                 employeeInProjectDto.setTotalNormalHours(totalNormal);
//                 employeeInProjectDto.setTotalOvertimeHours(totalOT);
//                 employeeInProjectDto.setTotalBonusHours(totalBonus);
//                 List<DailyWorkingHoursDto> dailyWorkingHoursDtos = new ArrayList<>();
//                 LocalDate start = yearMonth.atDay(1);
//                 LocalDate end = yearMonth.atEndOfMonth();
//                 for(LocalDate i = start; !i.isAfter(end) ; i = i.plusDays(1)){
//                     List<TaskEntity> taskList = taskRepository.findByUserIdAndProjectIdAndDateAndTimesheetStatus(
//                             userEntity.getId(), project.getId(), i, TimesheetStatus.ACCEPTED);
//                     DailyWorkingHoursDto dailyWorkingHoursDto = new DailyWorkingHoursDto();
//                     dailyWorkingHoursDto.setDate(i);
//                     double hour = taskList.stream().filter(t -> t.getTimesheetStatus() == TimesheetStatus.ACCEPTED).mapToDouble(TaskEntity::getWorkingHour).sum();
//                     dailyWorkingHoursDto.setHour(hour);
//                     dailyWorkingHoursDtos.add(dailyWorkingHoursDto);
//                 }
//                 employeeInProjectDto.setDailyWorkingHourDtos(dailyWorkingHoursDtos);
//                 employeeInProjectDtos.add(employeeInProjectDto);
//             }
//             reportDto.setEmployees(employeeInProjectDtos);
//             reportDtoList.add(reportDto);
//         }
//         int totalElements = reportDtoList.size();
//         int fromIndex = page * size;
//         int toIndex = Math.min(fromIndex + size, totalElements);
//         List<ReportDto> paginatedList = reportDtoList.subList(fromIndex, toIndex);
//         return new PageImpl<>(paginatedList, PageRequest.of(page, size), totalElements);
//     }
//     public void deleteTask(Long id){
//         TaskEntity task =  taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy task"));
//         taskRepository.delete(task);
//     }
// }
