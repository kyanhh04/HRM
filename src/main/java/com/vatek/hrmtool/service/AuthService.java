package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.AuthJwtResponse;
import com.vatek.hrmtool.entity.UserOld;
import org.springframework.security.core.Authentication;

public interface AuthService {
    AuthJwtResponse login(UserOld user, Authentication authentication);
}
