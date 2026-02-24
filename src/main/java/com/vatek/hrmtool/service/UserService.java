package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.UserDto.CreateUserRequest;
import com.vatek.hrmtool.dto.UserDto.GetUsersDto;
import com.vatek.hrmtool.dto.UserDto.UserPaginationResponse;
import com.vatek.hrmtool.dto.UserDto.UserResponseDto;
import com.vatek.hrmtool.dto.UserDto.UpdateUserDto;
import com.vatek.hrmtool.dto.UserDto.UpdatePasswordDto;
import com.vatek.hrmtool.dto.UserDto.ResetPassword;
import com.vatek.hrmtool.entity.UserOld;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface UserService {
    UserOld create(CreateUserRequest userRequest);
    UserPaginationResponse findAll(GetUsersDto params);
    UserPaginationResponse findOnboardingMentor(GetUsersDto params);
    UserResponseDto findOneWithoutAuth(String id);
    String changeAvatar(String userId, MultipartFile file);
    UserResponseDto update(String id, UpdateUserDto updateUserDto);
    UserResponseDto changePassword(String userId, UpdatePasswordDto updatePasswordDto);
    UserOld remove(String id);
    List<UserResponseDto> getBirthday();
    void resetPassword(ResetPassword resetPassword);
}
