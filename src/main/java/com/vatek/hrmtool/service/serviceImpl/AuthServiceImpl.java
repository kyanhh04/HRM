package com.vatek.hrmtool.service.serviceImpl;

import com.vatek.hrmtool.dto.RefreshTokenResponse;
import com.vatek.hrmtool.dto.LoginResponse;
import com.vatek.hrmtool.entity.Config;
import com.vatek.hrmtool.entity.UserOld;
import com.vatek.hrmtool.entity.ProjectOld;
import com.vatek.hrmtool.enumeration.StatusUser;
import com.vatek.hrmtool.jwt.JwtProvider;
import com.vatek.hrmtool.respository.old.UserOldRepository;
import com.vatek.hrmtool.respository.old.ProjectOldRepository;
import com.vatek.hrmtool.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserOldRepository userOldRepository;

    @Autowired
    private ProjectOldRepository projectOldRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public LoginResponse login(UserOld user, Authentication authentication){
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if(user.getStatus() == null || user.getStatus().equals(StatusUser.INACTIVE.getValue())){
            user.setStatus(StatusUser.INACTIVE.getValue());
            userOldRepository.save(user);
        }
        if(user.getStatus().equals(StatusUser.DEACTIVATED.getValue())){
            throw new IllegalStateException("Deleted Account can not login");
        }
        checkAndAssignDefaultProject(user);
        String access_token = jwtProvider.generateJwtToken(authentication);
        String refresh_token = jwtProvider.generateRefreshToken(authentication);
        user.setRefreshToken(refresh_token);
        userOldRepository.save(user);
        List<String> positions = getPositions(user);
        Long exp = jwtProvider.getRemainTimeFromJwtToken(access_token);
        return new LoginResponse(
                access_token,
                user.getId(),
                positions,
                exp
                );
    }
    public void checkAndAssignDefaultProject(UserOld user) {
        boolean hasProject = projectOldRepository.existsByMembers(user);

        if (!hasProject) {
            ProjectOld defaultProject = projectOldRepository.findByProjectName("Employee With No Project");
            if (defaultProject == null) {
                defaultProject = new ProjectOld();
                defaultProject.setProjectName("Employee With No Project");
                defaultProject.setCreatedBy(String.valueOf(user.getId()));
                defaultProject = projectOldRepository.save(defaultProject);
            }
            List<UserOld> members = defaultProject.getMembers();
            if (members == null) {
                members = new java.util.ArrayList<>();
                defaultProject.setMembers(members);
            }
            
            if (!members.contains(user)) {
                members.add(user);
                projectOldRepository.save(defaultProject);
            }
        }
    }
    public List<String> getPositions(UserOld user){
        List<Config> configs = user.getPositions();
        if (configs == null) {
            return new ArrayList<>();
        }
        List<String> positions = new ArrayList<>();
        for (Config config : configs) {
            positions.add(config.getValue());
        }
        return positions;
    }

    @Override
    public RefreshTokenResponse refresh(String refreshToken) {
        if (!jwtProvider.validateJwtToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }
        String userId = jwtProvider.getUserIdFromJwtToken(refreshToken);
        UserOld user = userOldRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
            throw new IllegalArgumentException("Refresh token doesn't match");
        }
        List<String> positions = getPositions(user);
        String newAccessToken = jwtProvider.generateTokenFromUserIdAndRole(user.getId(), positions);
        String newRefreshToken = jwtProvider.generateTokenFromUserIdAndRole(user.getId(), positions, 604800);
        user.setRefreshToken(newRefreshToken);
        userOldRepository.save(user);
        Long exp = jwtProvider.getRemainTimeFromJwtToken(newAccessToken);
        return new RefreshTokenResponse(
                newAccessToken,
                newRefreshToken,
                user.getId(),
                positions,
                exp
        );
    }

    @Override
    public void logout(String userId) {
        UserOld user = userOldRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRefreshToken(null);
        userOldRepository.save(user);
    }
}
