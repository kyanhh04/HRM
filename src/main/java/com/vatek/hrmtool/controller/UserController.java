package com.vatek.hrmtool.controller;


import com.vatek.hrmtool.dto.UserDto.*;
//import com.vatek.hrmtool.dto.UserDto.UserUpdateDto;
//import com.vatek.hrmtool.entity.neww.UserEntity;
//import com.vatek.hrmtool.enumeration.SortFieldName;
//import com.vatek.hrmtool.service.UserService;
//import com.vatek.hrmtool.service.serviceImpl.UserPrinciple;
import com.vatek.hrmtool.entity.UserOld;
import com.vatek.hrmtool.service.UserService;
import com.vatek.hrmtool.service.serviceImpl.UserOldPrinciple;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyAuthority('POSITION_ADMIN', 'POSITION_HR')")
    @PostMapping("/sign-up")
    public ResponseEntity<UserSignUpResponse> createUser(@RequestBody CreateUserRequest userRequest) {
        UserOld user = userService.create(userRequest);
        UserSignUpResponse response = mapToSignUpResponse(user);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('POSITION_ADMIN', 'POSITION_HR', 'POSITION_PM')")
    @GetMapping("/get-all-users")
    public ResponseEntity<UserPaginationResponse> getAllUsers(
        @RequestParam(required = false, defaultValue = "0") Integer offset,
        @RequestParam(required = false, defaultValue = "10") Integer limit,
        @RequestParam(required = false) String level,
        @RequestParam(required = false) List<String> positions,
        @RequestParam(required = false) String name) {
        GetUsersDto params = new GetUsersDto();
        params.setOffset(offset);
        params.setLimit(limit);
        params.setLevel(level);
        params.setPositions(positions);
        params.setName(name);
        UserPaginationResponse response = userService.findAll(params);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('POSITION_ADMIN', 'POSITION_HR')")
    @GetMapping("/get-onboarding-mentor")
    public ResponseEntity<UserPaginationResponse> findOnboardingMentor(
        @RequestParam(required = false, defaultValue = "0") Integer offset,
        @RequestParam(required = false, defaultValue = "10") Integer limit,
        @RequestParam(required = false) String name) {
        GetUsersDto params = new GetUsersDto();
        params.setOffset(offset);
        params.setLimit(limit);
        params.setName(name);
        UserPaginationResponse response = userService.findOnboardingMentor(params);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/get-own-profile")
    public ResponseEntity<UserResponseDto> findOwnProfile(@AuthenticationPrincipal UserOldPrinciple userPrinciple) {
        String userId = userPrinciple.getId();
        UserResponseDto user = userService.findOneWithoutAuth(userId);
        return ResponseEntity.ok(user);
        
    }

    @GetMapping("/user-avatar")
    public ResponseEntity<String> getUserAvatar(@AuthenticationPrincipal UserOldPrinciple userPrinciple) {
        String userId = userPrinciple.getId();
        UserResponseDto userData = userService.findOneWithoutAuth(userId);
        String avatarSrc = userData.getAvatar() != null ? userData.getAvatar().getSrc() : null;
        return ResponseEntity.ok(avatarSrc);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findOne(@PathVariable String id) {
        UserResponseDto user = userService.findOneWithoutAuth(id);
        return ResponseEntity.ok(user);
        
    }

    @PostMapping(value = "/change-avatar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> changeAvatar(@PathVariable String id, @RequestParam("avatar") MultipartFile file) {
        String avatarFileName = userService.changeAvatar(id, file);
        return ResponseEntity.ok(
                Map.of("userId", id,
                "avatar", avatarFileName));
    }

    @PreAuthorize("hasAnyAuthority('POSITION_ADMIN', 'POSITION_HR')")
    @PatchMapping("/update-user/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable String id, @RequestBody @Valid UpdateUserDto updateUserDto) {
        UserResponseDto responseDto = userService.update(id, updateUserDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<UserResponseDto> changePassword(@RequestBody @Valid UpdatePasswordDto updatePasswordDto, @AuthenticationPrincipal UserOldPrinciple userPrinciple) {
        String userId = userPrinciple.getId();
        UserResponseDto responseDto = userService.changePassword(userId, updatePasswordDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/update-profile")
    public ResponseEntity<UserResponseDto> updateProfile(@RequestBody @Valid UpdateUserDto updateUserDto, @AuthenticationPrincipal UserOldPrinciple userPrinciple) {
        String userId = userPrinciple.getId();
        UserResponseDto responseDto = userService.update(userId, updateUserDto);
        return ResponseEntity.ok(responseDto);
    }

    @PreAuthorize("hasAnyAuthority('POSITION_ADMIN', 'POSITION_HR')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> removeUser(@PathVariable String id) {
        UserOld deletedUser = userService.remove(id);
        return ResponseEntity.ok(Map.of(
            "deleteCount", 1,
            "userId", id,
            "username", deletedUser.getUsername()
        ));
    }

    @GetMapping("/get-birthday")
    public ResponseEntity<?> getBirthday() {
        List<UserResponseDto> users = userService.getBirthday();
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/reset_password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPassword resetPassword) {
        userService.resetPassword(resetPassword);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('POSITION_ADMIN', 'POSITION_HR')")
    @PatchMapping("/offboarding/{id}")
    public ResponseEntity<UserResponseDto> offboarding(@PathVariable String id) {
        return ResponseEntity.ok(userService.offboarding(id));
    }

    @PostMapping("forgot_password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid PayloadForgotPassword payload) {
        userService.sendEmailForgotPassword(payload.getEmail());
        return ResponseEntity.noContent().build();
    }

    private UserSignUpResponse mapToSignUpResponse(UserOld user) {
        UserSignUpResponse response = new UserSignUpResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setCitizenID(user.getCitizenID());
        response.setAddress(user.getAddress());
        response.setDateOfBirth(user.getDateOfBirth());
        response.setOnboardingDate(user.getOnboardingDate());
        response.setStatus(user.getStatus());
        return response;
    }

    // // Code cũ - dùng Role ADMIN
    // @PreAuthorize("hasRole('ADMIN')")
    // Code mới - dùng Position POSITION_ADMIN
//    @PreAuthorize("hasRole('POSITION_ADMIN')")
//    @PatchMapping("/update-user/{id}")
//    public ResponseEntity<UserUpdateDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto dto){
//        UserUpdateDto user = userService.updateEntities(id,dto);
//        return ResponseEntity.ok(user);
//    }
//    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'DEV_MEMBER', 'HR_MEMBER', 'MARKETING_MEMBER')")
//    @PatchMapping(value = "/change-avatar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> uploadAvatar(@RequestPart MultipartFile file, @PathVariable Long id) {
//        userService.uploadAvatar(file, id);
//        return ResponseEntity.ok("Upload image successfully");
//    }

     // Code cũ - dùng Role
    // @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'DEV_MEMBER', 'HR_MEMBER', 'MARKETING_MEMBER')")
    // Code mới - dùng Position
//    @PreAuthorize("hasAnyRole('POSITION_ADMIN', 'POSITION_PM', 'POSITION_DEV', 'POSITION_HR', 'POSITION_MARKETING')")
//    @PatchMapping("/change-password")
//    public ResponseEntity<?> changePassword(@RequestBody @Valid UpdatePasswordDto updatePasswordDto){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         // Code cũ - dùng UserPrinciple
        // UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        // Code mới - dùng userPrinciple
//        userPrinciple userPrinciple = (userPrinciple) authentication.getPrincipal();
//        Long userId = userPrinciple.getId();
//        userService.changePassword(userId, updatePasswordDto);
//        return ResponseEntity.ok("Đổi mật khẩu thành công");
//    }
//    @GetMapping("/get-user/{id}")
//    public ResponseEntity<?> getUserById(@PathVariable Long id){
//        UserEntity user =
//    }
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<?> deleteUser(@PathVariable Long id){
//        userService.deleteUser(id);
//        return ResponseEntity.noContent().build();
//    }
//    @GetMapping("/user-avatar")
//    public ResponseEntity<?> getUserAvatar(){
//
//    }
//    @GetMapping("/get-birthday")
//    public ResponseEntity<?> getBirthDayOfUser(){
//        LocalDate birthDay =  userService.getBirthDayOfUser();
//        return ResponseEntity.ok(birthDay);
//    }
    // // Code cũ - dùng Role ADMIN
    // @PreAuthorize("hasRole('ADMIN')")
    // Code mới - dùng Position POSITION_ADMIN
//    @PreAuthorize("hasRole('POSITION_ADMIN')")0
//}
     // Code cũ - dùng Role ADMIN
    // @PreAuthorize("hasRole('ADMIN')")
    // Code mới - dùng Position POSITION_ADMIN
//    @PreAuthorize("hasRole('POSITION_ADMIN')")
//    @PutMapping(value = "/update/{id}")
//    public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody @Valid EmployeeDto dto, @PathVariable Long id){
//        EmployeeDto employeeDto = userService.updateEmployee(dto, id);
//        return ResponseEntity.ok(employeeDto);
//    }
//    @GetMapping(value = "/get-all-users")
//    public ResponseEntity<Page<UserEntity>> listEmployee(@RequestParam(defaultValue = "")String keyword,
//                                                         @RequestParam(defaultValue = "ASC") Sort.Direction direction,
//                                                         @RequestParam(defaultValue = "NAME") SortFieldName sortBy,
//                                                         @RequestParam(defaultValue = "0")int page,
//                                                         @RequestParam(defaultValue = "10")int size){
//        Page<UserEntity> allEmployee = userService.listEmployee(keyword, direction, sortBy.getField(), page, size);
//        return ResponseEntity.ok(allEmployee);
//    }
}
