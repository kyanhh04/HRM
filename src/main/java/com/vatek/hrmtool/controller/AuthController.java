package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.dto.AuthJwtResponse;
import com.vatek.hrmtool.dto.LoginResponse;
import com.vatek.hrmtool.dto.RefreshTokenRequest;
import com.vatek.hrmtool.dto.UserDto.LoginDto;
//import com.vatek.hrmtool.entity.neww.UserEntity;
import com.vatek.hrmtool.entity.UserOld;
import com.vatek.hrmtool.jwt.JwtProvider;
//import com.vatek.hrmtool.respository.UserRepository;
import com.vatek.hrmtool.respository.old.UserOldRepository;
import com.vatek.hrmtool.service.AuthService;
import com.vatek.hrmtool.service.serviceImpl.UserOldPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserOldRepository userOldRepository;
    @Autowired
    private AuthService authService;

    //    @Autowired
//    private UserRepository userRepository;
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
//        try {
//            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
//            String jwt = jwtProvider.generateJwtToken(authentication);
//            UserEntity user = userRepository.findByUsernameOrEmail(loginDto.getUsername(), loginDto.getUsername())
//                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
//            user.setAccessToken(jwt);
//            user.setTokenStatus(true);
//            userRepository.save(user);
//            Long exp = jwtProvider.getRemainTimeFromJwtToken(jwt);
//            Date date = new Date(exp);
//            return ResponseEntity.ok(List.of(jwt, "expire time: " + date));
//        }
//        catch (BadCredentialsException e){
//            return  new ResponseEntity<>("Username or password is incorrect", HttpStatus.BAD_REQUEST);
//        }
//    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            if (loginDto.getUsername() == null || loginDto.getUsername().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Username is required"));
            }
            if (loginDto.getPassword() == null || loginDto.getPassword().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Password is required"));
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            UserOld user = userOldRepository.findByUsernameOrEmail(loginDto.getUsername(), loginDto.getPassword());
            LoginResponse loginResponse = authService.login(user, authentication);
            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest request) {
        try {
            if (request.getRefreshToken() == null || request.getRefreshToken().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Refresh token is required"));
            }
            AuthJwtResponse authJwtResponse = authService.refresh(request.getRefreshToken());
            return ResponseEntity.ok(authJwtResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired refresh token"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        try {
            String userId = getCurrentUserId();
            authService.logout(userId);
            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Logout failed: " + e.getMessage()));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken() {
        try {
            String userId = getCurrentUserId();
            boolean isActive = authService.verifyUserStatus(userId);
            if (!isActive) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "User account is deactivated or deleted", "active", false));
            }
            return ResponseEntity.ok(Map.of("message", "User is active", "active", true, "userId", userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Token verification failed: " + e.getMessage(), "active", false));
        }
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("User is not authenticated");
        }
        UserOldPrinciple userPrinciple = (UserOldPrinciple) authentication.getPrincipal();
        return userPrinciple.getId();
    }
}
