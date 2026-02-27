 package com.vatek.hrmtool.service.serviceImpl;

 import com.vatek.hrmtool.dto.UserDto.*;
 // import com.vatek.hrmtool.dto.UserDto.UserUpdateDto;
// import com.vatek.hrmtool.entity.neww.UserEntity;
// import com.vatek.hrmtool.entity.PositionEntity;
// import com.vatek.hrmtool.respository.RoleRepository;
// import com.vatek.hrmtool.respository.old.PositionRepository;
// import com.vatek.hrmtool.respository.UserRepository;
 import com.vatek.hrmtool.entity.Config;
 import com.vatek.hrmtool.entity.Image;
 import com.vatek.hrmtool.entity.ProjectOld;
 import com.vatek.hrmtool.entity.UserOld;
 import com.vatek.hrmtool.enumeration.RequestNotificationType;
 import com.vatek.hrmtool.enumeration.StatusUser;
 import com.vatek.hrmtool.notifications.MailNotificationsParam;
 import com.vatek.hrmtool.notifications.MailOptions;
 import com.vatek.hrmtool.respository.old.ConfigRepository;
 import com.vatek.hrmtool.respository.old.ProjectOldRepository;
 import com.vatek.hrmtool.respository.old.UserOldRepository;
 import com.vatek.hrmtool.service.MailService;
 import com.vatek.hrmtool.service.ProjectService;
 import com.vatek.hrmtool.service.UserService;
 import com.vatek.hrmtool.service.ImageService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.PageRequest;
 import org.springframework.data.domain.Pageable;
 import org.springframework.data.domain.Sort;
 import org.springframework.data.rest.webmvc.ResourceNotFoundException;
 import org.springframework.http.HttpStatus;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;
 import org.springframework.web.multipart.MultipartFile;
 import org.springframework.web.server.ResponseStatusException;
 import com.vatek.hrmtool.jwt.JwtProvider;

 import java.time.LocalDate;
 import java.time.LocalDateTime;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.stream.Collectors;

 @Service
 public class UserServiceImpl implements UserService {

  @Autowired
  private UserOldRepository userOldRepository;

  @Autowired
  private ConfigRepository configRepository;

  @Autowired
  private ProjectOldRepository projectOldRepository;

  @Autowired
  private ImageService imageService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtProvider jwtProvider;

  @Autowired
  private NotificationsService notificationsService;

  @Autowired
  private MailService mailService;

  private static final String DEFAULT_PASSWORD = "1]Pz+Ei0qPPM7G)z";

  @Transactional
   public UserOld create(CreateUserRequest request) {
    boolean existingByUsername = userOldRepository.findByUsername(request.getUsername()).isPresent();
    boolean existingByEmail = userOldRepository.findByEmail(request.getEmail()).isPresent();
    if (existingByUsername || existingByEmail) {
     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or email already exists");
    }

    String hashedPassword = passwordEncoder.encode(DEFAULT_PASSWORD);
    String[] nameParts = request.getFullName().split(" ");
    String name = nameParts[nameParts.length - 1];
    UserOld newUser = new UserOld();
    newUser.setUsername(request.getUsername());
    newUser.setEmail(request.getEmail());
    newUser.setFullName(request.getFullName());
    newUser.setName(name);
    newUser.setPasswordHash(hashedPassword);
    newUser.setCreatedBy("admin");
    newUser.setDateCreated(LocalDateTime.now());
    newUser.setAddress(request.getAddress());
    newUser.setDateOfBirth(request.getDateOfBirth());
    newUser.setOnboardingDate(request.getOnboardingDate());
    newUser.setPhone(request.getPhone());
    newUser.setCitizenID(request.getCitizenID());
    if (request.getOnboardingMentor() != null && !request.getOnboardingMentor().isEmpty()) {
        userOldRepository.findById(request.getOnboardingMentor()).ifPresent(newUser::setOnboardingMentor);
    }

    if(request.getLevelId() != null && !request.getLevelId().isEmpty()){
        configRepository.findById(request.getLevelId()).ifPresent(newUser::setLevel);
    }

    if(request.getPositions() != null && !request.getPositions().isEmpty()){
        List<Config> positions = configRepository.findAllById(request.getPositions());
        newUser.setPositions(positions);
    }

    MailNotificationsParam param = new MailNotificationsParam();
    param.setReceivers(new String[] {request.getEmail()});
    param.setUsername(newUser.getUsername());
    param.setPassword(DEFAULT_PASSWORD);
    param.setFullName(newUser.getFullName());
    param.setNotificationsType(RequestNotificationType.NEW_ACCOUNT);

    MailOptions mailOptions = notificationsService.handleParseMailOption(param);
    notificationsService.sendMail(mailOptions);

    return userOldRepository.save(newUser);
   }
//     @Autowired
//     private UserRepository userRepository;
//     @Autowired
//     private ObjectMapper objectMapper;
//     @Autowired
//     private PasswordEncoder passwordEncoder;
//     @Autowired
//     private RoleRepository roleRepository;
//     @Autowired
//     private PositionRepository positionRepository;
//     @Autowired
//     private ModelMapper modelMapper;
//     @Override
//     public UserUpdateDto updateEntities(Long id, UserUpdateDto dto){
//         UserEntity user = userRepository.findUserEntityById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy User"));
//         if(dto.getPassword()!=null){
//             user.setPassword(passwordEncoder.encode(dto.getPassword()));
//         }
//         if(dto.getName()!= null){
//             user.setName(dto.getName());
//         }
//         if(dto.getEmail() != null){
//             user.setEmail(dto.getEmail());
//         }
//         if(dto.getLevel() != null) {
//             user.setLevel(dto.getLevel());
//         }
//         if(dto.getAvatarUrl() != null){
//             user.setAvatarUrl(dto.getAvatarUrl());
//         }
//         if(dto.getCurrentAddress() != null){
//             user.setCurrentAddress(dto.getCurrentAddress());
//         }
//         if(dto.getIdentityCard() != null){
//             user.setIdentityCard(dto.getIdentityCard());
//         }
//         if(dto.getPosition() != null){
//             user.setPosition(dto.getPosition());
//         }
//         if(dto.getPermanentAddress() != null){
//             user.setPermanentAddress(dto.getPermanentAddress());
//         }
//         if(dto.getPhoneNumber1() != null){
//             user.setPhoneNumber1(dto.getPhoneNumber1());
//         }
//         if(dto.getUsername()!= null) {
//             user.setUsername(dto.getUsername());
//         }
//         if(dto.getProgramLanguage() != null){
//             user.setProgramLanguage(dto.getProgramLanguage());
//         }
//         if(dto.isEnabled()){
//             user.setEnabled(true);
//         }
//         if(dto.isTokenStatus()){
//             user.setTokenStatus(true);
//         }
//         if(dto.getStartTime() != null){
//             user.setStartTime(dto.getStartTime());
//         }
//         if(dto.getEndTime() != null){
//             user.setEndTime(dto.getEndTime());
//         }
//         if(dto.getDateOfBirth() != null){
//             user.setDateOfBirth(dto.getDateOfBirth());
//         }
//         // // Code cũ - dùng roles
//         // if(dto.getRoles() != null){
//         //     Set<RoleEntity> roles = dto.getRoles().stream()
//         //             .map(roleRepository::findByRole)
//         //             .collect(Collectors.toSet());
//         //     user.setRoles(roles);
//         // }
//         // Code mới - dùng positions
//         if(dto.getPositions() != null){
//             Set<PositionEntity> positions = dto.getPositions().stream()
//                     .map(positionRepository::findByPosition)
//                     .collect(Collectors.toSet());
//             // // Cảnh báo: UserEntity không có setPositions, cạn xử lý
//             // user.setPositions(positions);
//         }
//         user = userRepository.save(user);
//         UserUpdateDto userUpdateDto = new UserUpdateDto();
//         BeanUtils.copyProperties(user, userUpdateDto);
//         // // Code cũ
//         // Set<Role> roles = user.getRoles().stream()
//         //         .map(RoleEntity::getRole).collect(Collectors.toSet());
//         // userUpdateDto.setRoles(roles);
//         // Code mới - sử dụng positions nếu có
//         return userUpdateDto;
//     }
//     @Override
//     public void changePassword(Long userId, UpdatePasswordDto dto){
//         UserEntity user = userRepository.findById(userId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
//         if(!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())){
//             throw new HttpMessageNotReadableException("Password is incorrect");
//         }
//         user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
//         userRepository.save(user);
//     }
//     @Override
//     public EmployeeDto createEmployee(EmployeeDto employeeDto){
//         UserEntity user = new UserEntity();
//         if(userRepository.existsByEmail(employeeDto.getEmail())){
//             throw new DuplicateKeyException("Email đã tồn tại");
//         }
//         if(userRepository.existsByIdentityCard(employeeDto.getIdentityCard())){
//             throw new DuplicateKeyException("IdentityCard đã tồn tại");
//         }
//         if(userRepository.existsByUsername(employeeDto.getUsername())){
//             throw new DuplicateKeyException("Username đã tồn tại");
//         }
//         BeanUtils.copyProperties(employeeDto, user, "password");
//         user.setEnabled(true);
//         user.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
//         // // Code cũ - dùng roles
//         // Set<RoleEntity> roles = employeeDto.getRoles().stream().map(role -> roleRepository.findByRole(role)).collect(Collectors.toSet());
//         // user.setRoles(roles);
//         // Code mới - dùng positions
//         Set<PositionEntity> positions = employeeDto.getPositions().stream().map(position -> positionRepository.findByPosition(position)).collect(Collectors.toSet());
//         // // Cảnh báo: UserEntity không có setPositions
//         // user.setPositions(positions);
//         if(employeeDto.getStartTime() != null){
//             user.setStartTime(employeeDto.getStartTime());
//         }
//         else{
//             user.setStartTime(LocalTime.of(8,30));
//         }
//         if(employeeDto.getEndTime() != null){
//             user.setEndTime(employeeDto.getEndTime());
//         }
//         else{
//             user.setEndTime(LocalTime.of(18, 0));
//         }
//         userRepository.save(user);
//         BeanUtils.copyProperties(user, employeeDto);
//         employeeDto.setStartTime(user.getStartTime());
//         employeeDto.setEndTime(user.getEndTime());
//         return employeeDto;
//     }
//     @Override
//     public void uploadAvatar(MultipartFile file, Long id){
//         if(file.isEmpty()){
//             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file rỗng");
//         }
//         String contentType = file.getContentType();
//         if (contentType == null || !contentType.startsWith("image/")) {
//             throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Chỉ hỗ trợ file ảnh");
//         }
//         try{
//             String fileName = file.getOriginalFilename();
//             Path path = Paths.get("uploads").resolve(fileName);
//             Files.write(path, file.getBytes());
//             UserEntity user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
//             user.setAvatarUrl(fileName);
//             userRepository.save(user);
//         }
//         catch (IOException ex){
//             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "message: " + ex);
//         }
//     }
//     @Override
//     public EmployeeDto updateEmployee(EmployeeDto employeeDto, Long id){
//         UserEntity user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy user"));
//         List<UserEntity> allUser = userRepository.findAll();
//         for(UserEntity userEntity : allUser){
//             if(userEntity.getId().equals(id)){
//                 continue;
//             }
//             if(employeeDto.getUsername().equals(userEntity.getUsername())){
//                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username đã tồn tại");
//             }
//             if(employeeDto.getIdentityCard().equals(userEntity.getIdentityCard())){
//                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IdentityCard đã tồn tại");
//             }
//             if(employeeDto.getEmail().equals(userEntity.getEmail())){
//                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại");
//             }
//         }
//         BeanUtils.copyProperties(employeeDto, user, "id", "password");
//         user.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
//         // // Code cũ - dùng roles
//         // Set<RoleEntity> roles = employeeDto.getRoles().stream().map(role -> roleRepository.findByRole(role)).collect(Collectors.toSet());
//         // user.setRoles(roles);
//         // Code mới - dùng positions
//         Set<PositionEntity> positions = employeeDto.getPositions().stream().map(position -> positionRepository.findByPosition(position)).collect(Collectors.toSet());
//         // // Cảnh báo: UserEntity không có setPositions
//         // user.setPositions(positions);
//         user = userRepository.save(user);
//         EmployeeDto dto = modelMapper.map(user, EmployeeDto.class);
//         // // Code cũ
//         // dto.setRoles(employeeDto.getRoles());
//         // Code mới
//         dto.setPositions(employeeDto.getPositions());
//         return dto;
//     }
//     @Override
//     public Page<UserEntity> listEmployee(String keyword, Sort.Direction direction, String sortBy, int page, int size){
//         Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
//         if(keyword.isBlank()){
//             return userRepository.findAll(pageable);
//         }
//         return userRepository.searchByKeyword(keyword, pageable);
//     }
//     @Override
//     public void deleteUser(Long userId){
//         UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
//         userRepository.delete(user);
//     }

   @Override
   public UserPaginationResponse findAll(GetUsersDto params) {
       long total = userOldRepository.countByIsDeletedFalse();
       Pageable pageable = PageRequest.of(
           params.getOffset(),
           params.getLimit(),
           Sort.by("name").ascending()
       );
       Page<UserOld> page;
       if (params.getPositions() != null && !params.getPositions().isEmpty()) {
           page = userOldRepository.findAllWithFiltersAndPositions(
               params.getLevel(),
               params.getPositions(),
               params.getName(),
               pageable
           );
       } else {
           page = userOldRepository.findAllWithFilters(
               params.getLevel(),
               params.getName(),
               pageable
           );
       }
       long paginateTotal = page.getTotalElements();
       List<UserResponseDto> data = page.getContent().stream()
           .map(this::mapToUserResponseDto)
           .collect(Collectors.toList());
       return new UserPaginationResponse(data, total, paginateTotal);
   }

   public UserResponseDto mapToUserResponseDto(UserOld user) {
       UserResponseDto dto = new UserResponseDto();
       dto.setId(user.getId());
       dto.setFullName(user.getFullName());
       dto.setEmail(user.getEmail());
       dto.setOnboardingDate(user.getOnboardingDate());
       dto.setPhone(user.getPhone());
       dto.setName(user.getName());
       dto.setDateOfBirth(user.getDateOfBirth());
       dto.setStatus(user.getStatus());
       if (user.getOnboardingMentor() != null) {
           UserResponseDto.UserMentorDto mentorDto = new UserResponseDto.UserMentorDto();
           mentorDto.setId(user.getOnboardingMentor().getId());
           mentorDto.setFullName(user.getOnboardingMentor().getFullName());
           mentorDto.setEmail(user.getOnboardingMentor().getEmail());
           dto.setOnboardingMentor(mentorDto);
       }
       if (user.getLevel() != null) {
           UserResponseDto.ConfigDto levelDto = new UserResponseDto.ConfigDto();
           levelDto.setId(user.getLevel().getId());
           levelDto.setKey(user.getLevel().getKey());
           levelDto.setValue(user.getLevel().getValue());
           dto.setLevel(levelDto);
       }
       if (user.getPositions() != null && !user.getPositions().isEmpty()) {
           List<UserResponseDto.ConfigDto> positionDtos = user.getPositions().stream()
               .map(position -> {
                   UserResponseDto.ConfigDto posDto = new UserResponseDto.ConfigDto();
                   posDto.setId(position.getId());
                   posDto.setKey(position.getKey());
                   posDto.setValue(position.getValue());
                   return posDto;
               })
               .collect(Collectors.toList());
           dto.setPositions(positionDtos);
       }

       if (user.getAvatar() != null) {
           UserResponseDto.ImageDto avatarDto = new UserResponseDto.ImageDto();
           avatarDto.setId(user.getAvatar().getId());
           avatarDto.setSrc(user.getAvatar().getSrc());
           dto.setAvatar(avatarDto);
       }

       return dto;
   }

   public UserPaginationResponse findOnboardingMentor(GetUsersDto params) {
       List<Config> adminHrPositions = configRepository.findAll().stream()
           .filter(config -> "POSITION".equals(config.getKey()) && 
                   ("POSITION_ADMIN".equals(config.getValue()) || "POSITION_HR".equals(config.getValue())))
           .toList();
       if (adminHrPositions.isEmpty()) {
           throw new IllegalArgumentException("ADMIN or HR position not found");
       }
       List<String> positionIds = adminHrPositions.stream()
           .map(Config::getId)
           .collect(Collectors.toList());
       Pageable pageable = PageRequest.of(
           params.getOffset(),
           params.getLimit(),
           Sort.by("name").ascending()
       );
       long total = userOldRepository.countByIsDeletedFalse();
       Page<UserOld> page;
       if (params.getName() != null && !params.getName().isEmpty()) {
           page = userOldRepository.findByNameAndPositionsAndIsDeletedFalse(
               params.getName(),
               positionIds, 
               pageable
           );
       } else {
           page = userOldRepository.findByPositionsAndIsDeletedFalse(
               positionIds, 
               pageable
           );
       }
       
       long paginateTotal = page.getTotalElements();
       List<UserResponseDto> data = page.getContent().stream()
           .map(this::mapToUserResponseDto)
           .collect(Collectors.toList());
       
       return new UserPaginationResponse(data, total, paginateTotal);
   }

   public UserResponseDto findOneWithoutAuth(String id) {
       UserOld user = userOldRepository.findByIdAndIsDeletedFalse(id)
           .orElseThrow(() -> new ResourceNotFoundException("Cannot find user with id " + id));
       return mapToUserResponseDto(user);
   }

     public String changeAvatar(String userId, MultipartFile file){
       UserOld user = userOldRepository.findByIdAndIsDeletedFalse(userId)
           .orElseThrow(() -> new ResourceNotFoundException("Cannot find user with id " + userId));
       Image image = imageService.uploadAndCreate(file, userId);
       user.setAvatar(image);
       userOldRepository.save(user);
       return image.getSrc();
   }

   public UserResponseDto update(String id, UpdateUserDto updateUserDto) {
       UserOld user = userOldRepository.findByIdAndIsDeletedFalse(id)
           .orElseThrow(() -> new ResourceNotFoundException("Cannot find user with id " + id));
       if (updateUserDto.getFullName() != null && !updateUserDto.getFullName().isEmpty()) {
           String fullName = updateUserDto.getFullName();
           user.setFullName(fullName);
           String[] nameParts = fullName.split(" ");
           String name = nameParts[nameParts.length - 1];
           user.setName(name);
       }
       if (updateUserDto.getEmail() != null && !updateUserDto.getEmail().isEmpty()) {
           user.setEmail(updateUserDto.getEmail());
       }
       if (updateUserDto.getPhone() != null && !updateUserDto.getPhone().isEmpty()) {
           user.setPhone(updateUserDto.getPhone());
       }
       if (updateUserDto.getPassword() != null && !updateUserDto.getPassword().isEmpty()) {
           String hashedPassword = passwordEncoder.encode(updateUserDto.getPassword());
           user.setPasswordHash(hashedPassword);
       }
       if (updateUserDto.getCitizenID() != null && !updateUserDto.getCitizenID().isEmpty()) {
           user.setCitizenID(updateUserDto.getCitizenID());
       }
       if (updateUserDto.getAddress() != null && !updateUserDto.getAddress().isEmpty()) {
           user.setAddress(updateUserDto.getAddress());
       }
       if (updateUserDto.getDateOfBirth() != null) {
           user.setDateOfBirth(updateUserDto.getDateOfBirth());
       }
       if (updateUserDto.getOnboardingDate() != null) {
           user.setOnboardingDate(updateUserDto.getOnboardingDate());
       }
       if (updateUserDto.getOnboardingMentor() != null && !updateUserDto.getOnboardingMentor().isEmpty()) {
           UserOld mentor = userOldRepository.findByIdAndIsDeletedFalse(updateUserDto.getOnboardingMentor())
               .orElse(null);
           user.setOnboardingMentor(mentor);
       }
       if (updateUserDto.getLevel() != null && !updateUserDto.getLevel().isEmpty()) {
           configRepository.findById(updateUserDto.getLevel()).ifPresent(user::setLevel);
       }
       if (updateUserDto.getPositions() != null && !updateUserDto.getPositions().isEmpty()) {
           java.util.List<Config> positions = configRepository.findAllById(updateUserDto.getPositions());
           user.setPositions(positions);
       }
       UserOld updatedUser = userOldRepository.save(user);
       return mapToUserResponseDto(updatedUser);
   }

   public UserResponseDto changePassword(String userId, UpdatePasswordDto updatePasswordDto) {
       UserOld user = userOldRepository.findByIdAndIsDeletedFalse(userId)
           .orElseThrow(() -> new ResourceNotFoundException("Cannot find user with id " + userId));
       if (!passwordEncoder.matches(updatePasswordDto.getCurrentPassword(), user.getPasswordHash())) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is invalid");
       }
       String hashedPassword = passwordEncoder.encode(updatePasswordDto.getPassword());
       user.setPasswordHash(hashedPassword);
       UserOld updatedUser = userOldRepository.save(user);
       return mapToUserResponseDto(updatedUser);
   }

   public UserOld remove(String id) {
       UserOld currentUser = userOldRepository.findByIdAndIsDeletedFalse(id)
           .orElseThrow(() -> new ResourceNotFoundException("Cannot find user"));
       
       if (currentUser.getPositions() != null && currentUser.getPositions().stream()
           .anyMatch(p -> "POSITION_ADMIN".equals(p.getValue()))) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete admin account");
       }
       
       List<ProjectOld> projects = projectOldRepository.findAll();
       for (ProjectOld project : projects) {
           if (!project.getIsDeleted()) {
               if (project.getProjectManager() != null && project.getProjectManager().getId().equals(id)) {
                   throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + id + " is PM of project: " + project.getProjectName());
               }
               if (project.getMembers() != null && project.getMembers().stream().anyMatch(m -> m.getId().equals(id))) {
                   throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + id + " is PM of project: " + project.getProjectName());
               }
           }
       }
       
       currentUser.setIsDeleted(true);
       return userOldRepository.save(currentUser);
   }

   @Override
   @Transactional
   public UserResponseDto offboarding(String id) {
       UserOld currentUser = userOldRepository.findByIdAndIsDeletedFalse(id)
               .orElseThrow(() -> new ResourceNotFoundException("Cannot find user with id " + id));

       // Don't allow offboarding admin accounts
       if (currentUser.getPositions() != null && currentUser.getPositions().stream()
               .anyMatch(p -> "POSITION_ADMIN".equals(p.getValue()))) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot offboard admin account");
       }

       // Find projects where user is manager or member
       List<ProjectOld> projects = projectOldRepository.findByUserAsManagerOrMember(id);
       if (projects != null) {
           for (ProjectOld project : projects) {
               if (project.getIsDeleted() != null && project.getIsDeleted()) continue;

               if (project.getMembers() != null && !project.getMembers().isEmpty()) {
                   boolean removed = project.getMembers().removeIf(m -> id.equals(m.getId()));
                   if (removed) {
                       projectOldRepository.save(project);
                   }
               }
           }
       }

       currentUser.setStatus(StatusUser.DEACTIVATED.getValue());
       return mapToUserResponseDto(userOldRepository.save(currentUser));
   }

     @Override
     public void sendEmailForgotPassword(String email) {
         UserOld user = userOldRepository.findByEmailAndIsDeletedFalse(email)
             .orElseThrow(() -> new ResourceNotFoundException("Cannot find user with email " + email));

         String token = jwtProvider.generateToken(user.getUsername(), user.getId());
         String replacedHtml = mailService.forgotPasswordTemplate(user.getFullName(), token);
         MailNotificationsParam param = new MailNotificationsParam();
         param.setReceivers(new String[] {user.getEmail()});
         param.setUsername(user.getUsername());
         param.setFullName(user.getFullName());
         param.setReplacedHtml(replacedHtml);
         param.setNotificationsType(RequestNotificationType.FORGOT_PASSWORD);

         MailOptions mailOptions = notificationsService.handleParseMailOption(param);
         notificationsService.sendMail(mailOptions);
     }

     public List<UserResponseDto> getBirthday() {
       int today = LocalDate.now().getDayOfMonth();
       int currentMonth = LocalDate.now().getMonthValue();
       List<UserOld> users = userOldRepository.findAll().stream()
           .filter(u -> !u.getIsDeleted())
           .toList();
       List<UserResponseDto> birthdayUsers = new ArrayList<>();
       for (UserOld user : users) {
           if (user.getDateOfBirth() != null) {
               if (user.getDateOfBirth().getDayOfMonth() == today && 
                   user.getDateOfBirth().getMonthValue() == currentMonth) {
                   birthdayUsers.add(mapToUserResponseDto(user));
               }
           }
       }
       return birthdayUsers;
   }

   public void resetPassword(ResetPassword resetPassword) {
       String userId = jwtProvider.getUserIdFromJwtToken(resetPassword.getToken());
       if (userId == null || userId.isEmpty()) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
       }
       UserOld user = userOldRepository.findByIdAndIsDeletedFalse(userId)
           .orElseThrow(() -> new ResourceNotFoundException("Cannot find user with id " + userId));
       String hashedPassword = passwordEncoder.encode(resetPassword.getNewPassword());
       user.setPasswordHash(hashedPassword);
       userOldRepository.save(user);
   }
 }

