package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.dto.AuthJwtResponse;
import com.vatek.hrmtool.dto.UserDto.LoginDto;
//import com.vatek.hrmtool.entity.neww.UserEntity;
import com.vatek.hrmtool.entity.UserOld;
import com.vatek.hrmtool.jwt.JwtProvider;
//import com.vatek.hrmtool.respository.UserRepository;
import com.vatek.hrmtool.respository.old.UserOldRepository;
import com.vatek.hrmtool.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

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
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        UserOld user = userOldRepository.findByUsernameOrEmail(loginDto.getUsername(), loginDto.getPassword());
        AuthJwtResponse authJwtResponse = authService.login(user, authentication);
        return ResponseEntity.ok(authJwtResponse);
    }
}
