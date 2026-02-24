package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.RefreshTokenResponse;
import com.vatek.hrmtool.dto.LoginResponse;
import com.vatek.hrmtool.entity.UserOld;
import org.springframework.security.core.Authentication;

public interface AuthService {
    LoginResponse login(UserOld user, Authentication authentication);
    RefreshTokenResponse refresh(String refreshToken);
    void logout(String userId);
}
