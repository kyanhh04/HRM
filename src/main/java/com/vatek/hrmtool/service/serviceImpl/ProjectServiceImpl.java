// package com.vatek.hrmtool.service.serviceImpl;

// import com.vatek.hrmtool.dto.ProjectDto.*;
// import com.vatek.hrmtool.entity.neww.ProjectEntity;
// import com.vatek.hrmtool.entity.neww.RoleEntity;
// import com.vatek.hrmtool.entity.neww.UserEntity;
// import com.vatek.hrmtool.enumeration.Role;
// import com.vatek.hrmtool.enumeration.ProjectState;
// import com.vatek.hrmtool.respository.ProjectRepository;
// import com.vatek.hrmtool.respository.UserRepository;
// import com.vatek.hrmtool.service.ProjectService;
// import org.modelmapper.ModelMapper;
// import org.springframework.beans.BeanUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
// import org.springframework.data.rest.webmvc.ResourceNotFoundException;
// import org.springframework.stereotype.Service;

// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.Collection;
// import java.util.Set;
// import java.util.stream.Collectors;

// @Service
// public class ProjectServiceImpl implements ProjectService {
//     @Autowired
//     private UserRepository userRepository;
//     @Autowired
//     private ProjectRepository projectRepository;
//     @Autowired
//     private ModelMapper modelMapper;
//     @Override
//     public CreateProjectDto createProject(CreateProjectDto dto){
//         ProjectEntity project = new ProjectEntity();
//         BeanUtils.copyProperties(dto,project, "status", "user", "endDate");
//         UserEntity userEntity = userRepository.findById(dto.getPmId())
//                         .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
//         boolean check = false;
//         // // Code cũ - kiểm tra Role
//         // for(RoleEntity roleEntity : userEntity.getRoles()){
//         //     Role role = roleEntity.getRole();
//         //     if(role.equals(Role.PROJECT_MANAGER)){
//         //         check = true;
//         //     }
//         // }
//         // NÓT: UserEntity vẫn dùng Role, tạm thời bỏ qua check Role
//         check = true;
//         if(!check){
//             throw new IllegalArgumentException("Người dùng không có vai trò là PM");
//         }
//         if(dto.getStartDate() != null && dto.getStartDate().isBefore(LocalDate.now())){
//             throw new IllegalArgumentException("Ngày bắt đầu không được nhỏ hơn ngày hiện tại");
//         }
//         if(dto.getStartDate() == null){
//             project.setStartDate(null);
//         }
//         project.setEndDate(null);
//         project.setProjectManager(userEntity);
//         project.setProjectState(ProjectState.RUNNING);
//         Collection<UserEntity> users = new ArrayList<>();
//         if(dto.getMemberId() != null){
//             for (Long id : dto.getMemberId()) {
//                 UserEntity user = userRepository.findById(id)
//                         .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
//                 users.add(user);
//             }
//         }
//         project.setUsers(users);
//         project = projectRepository.save(project);
//         BeanUtils.copyProperties(project, dto);
//         return dto;
//     }
//     @Override
//     public UpdateProjectDto updateProject(UpdateProjectDto dto, Long id){
//         ProjectEntity project = projectRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy project"));

//         BeanUtils.copyProperties(dto, project, "id");
//         if(dto.getStartDate() != null && dto.getEndDate() != null && dto.getStartDate().isAfter(dto.getEndDate())){
//             throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kết thúc");
//         }
//         if(dto.getEndDate() != null && dto.getEndDate().isBefore(LocalDate.now())){
//             throw new IllegalArgumentException("Ngày kết thúc không được trước ngày hiện tại");
//         }
//         if(project.getStartDate() == null && dto.getEndDate() != null){
//             throw new IllegalArgumentException("Vui lòng điền ngày bắt đầu");
//         }
//         if(dto.getStartDate() == null && dto.getEndDate() != null){
//             throw new IllegalArgumentException("Vui lòng điền ngày bắt đầu");
//         }
//         if(dto.getProjectState() == ProjectState.DONE && dto.getEndDate() != null){
//             project.setProjectState(ProjectState.DONE);
//             project.setEndDate(dto.getEndDate());
//         }
//         if(dto.getProjectState() == ProjectState.DONE && dto.getEndDate() == null){
//             project.setProjectState(ProjectState.DONE);
//             project.setEndDate(LocalDate.now());
//         }
//         Collection<UserEntity> users = new ArrayList<>();
//         project.setUsers(users);
//         UserEntity userEntity = userRepository.findById(dto.getPmId())
//                 .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
//         boolean check = false;
//         // // Code cũ - kiểm tra Role
//         // for(RoleEntity roleEntity : userEntity.getRoles()){
//         //     Role role = roleEntity.getRole();
//         //     if(role.equals(Role.PROJECT_MANAGER)){
//         //         check = true;
//         //     }
//         // }
//         // NÓT: UserEntity vẫn dùng Role, tạm thời bỏ qua check Role
//         check = true;
//         if(!check){
//             throw new IllegalArgumentException("Người dùng không có vai trò là PM");
//         }
//         project.setProjectManager(userEntity);
//         project = projectRepository.save(project);
//         BeanUtils.copyProperties(project, dto);
//         return dto;
//     }
//     @Override
//     public void addMember(Long projectId, ProjectEmployeeDto dto){
//         UserEntity user = userRepository.findById(dto.getMemberId())
//                 .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));
//         ProjectEntity project = projectRepository.findById(projectId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dự án"));
//         boolean check = false;
//         // // Code cũ - kiểm tra Role
//         // for(RoleEntity roleEntity : user.getRoles()){
//         //     if(roleEntity.getRole() != Role.DEV_MEMBER && roleEntity.getRole() != Role.PROJECT_MANAGER && roleEntity.getRole() != Role.ADMIN){
//         //         check = true;
//         //     }
//         // }
//         // NÓT: Tạm thời bỏ qua kiểm tra Role
//         check = false;
//         if(check){
//             throw new IllegalArgumentException("Người dùng không phải là DEV hoặc PM");
//         }
//         Collection<UserEntity> users = new ArrayList<>(project.getUsers());
//         users.add(user);
//         project.setUsers(users);
//         projectRepository.save(project);
//     }
//     @Override
//     public void deleteMember(Long projectId, ProjectEmployeeDto dto){
//         userRepository.findById(dto.getMemberId())
//                 .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));
//         projectRepository.findById(projectId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dự án"));
//         projectRepository.removeMemberFromProject(projectId, dto.getMemberId());
//     }
//     @Override
//     public Page<GetListProjectDto> getAllProject(String keyword, Sort.Direction direction, String startDate, int page, int size){
//         Pageable pageable = PageRequest.of(page, size, Sort.by(direction, startDate));
//         Page<ProjectEntity> projects;
//         if(!keyword.isBlank()){
//             projects = projectRepository.searchByKeyword(keyword, pageable);
//         }
//         else{
//             projects = projectRepository.findAll(pageable);
//         }
//         return projects.map(this::convertToListProjectDto);
//     }

//     @Override
//     public Page<GetListProjectDto> getAllProjects(String projectName, int page, int size){
//         Pageable pageable = PageRequest.of(page, size);
//         Page<ProjectEntity> projects;
//         if(!projectName.isBlank()){
//             projects = projectRepository.searchByKeyword(projectName, pageable);
//         }
//         else{
//             projects = projectRepository.findAll(pageable);
//         }
//         return projects.map(this::convertToListProjectDto);
//     }
//     public GetListProjectDto convertToListProjectDto(ProjectEntity project){
//         GetListProjectDto getListProjectDto = new GetListProjectDto();
//         getListProjectDto.setId(project.getId());
//         getListProjectDto.setProjectManager(project.getProjectManager().getName());
//         getListProjectDto.setProjectName(project.getProjectName());
//         getListProjectDto.setProjectState(project.getProjectState());
//         getListProjectDto.setClientName(project.getClientName());
//         if(project.getStartDate() != null){
//             getListProjectDto.setStartDate(project.getStartDate());
//         }
//         if(project.getEndDate() != null){
//             getListProjectDto.setEndDate(project.getEndDate());
//         }
//         return getListProjectDto;
//     }

//     @Override
//     public ProjectDetailDto getProjectDetail(Long id) {
//         ProjectEntity project = projectRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dự án"));
//         ProjectDetailDto projectDetailDto = new ProjectDetailDto();
//         BeanUtils.copyProperties(project, projectDetailDto, "startDate", "endDate");
//         projectDetailDto.setProjectManager(project.getProjectManager().getName());
//         if(project.getStartDate() != null){
//             projectDetailDto.setStartDate(project.getStartDate());
//         }
//         if(project.getEndDate() != null){
//             projectDetailDto.setEndDate(project.getEndDate());
//         }        Set<String> members = project.getUsers().stream().map(UserEntity::getName).collect(Collectors.toSet());
//         projectDetailDto.setProjectMembers(members);
//         return projectDetailDto;
//     }
//     @Override
//     public void deleteProject(Long id){
//         ProjectEntity project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy project"));
//         projectRepository.delete(project);
//     }
// }
