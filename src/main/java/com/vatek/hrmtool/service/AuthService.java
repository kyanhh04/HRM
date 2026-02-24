package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.AuthJwtResponse;
import com.vatek.hrmtool.dto.LoginResponse;
import com.vatek.hrmtool.entity.UserOld;
import org.springframework.security.core.Authentication;

public interface AuthService {
    LoginResponse login(UserOld user, Authentication authentication);
    AuthJwtResponse refresh(String refreshToken);
    void logout(String userId);
    boolean verifyUserStatus(String userId);
}
