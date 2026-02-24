package com.vatek.hrmtool.jwt;

import com.vatek.hrmtool.entity.UserOld;
import com.vatek.hrmtool.respository.old.UserOldRepository;
import com.vatek.hrmtool.service.serviceImpl.UserDetailsServiceImpl;
import com.vatek.hrmtool.service.serviceImpl.UserOldPrinciple;
//import com.vatek.hrmtool.respository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider tokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    UserOldRepository userOldRepository;
    
    // // Code cũ
    // @Autowired
    // UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwt(request);

            if (jwt == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!tokenProvider.validateJwtToken(jwt)) {
                filterChain.doFilter(request, response);
                return;
            }
            String userId = tokenProvider.getUserIdFromJwtToken(jwt);
            // UserEntity user = userRepository.findUserEntityById(userId)
            //         .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
            UserOld user = userOldRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
            UserOldPrinciple userDetails = (UserOldPrinciple) userDetailsService.loadUserByUsername(user.getEmail());
            userDetails.setRemainTime(tokenProvider.getRemainTimeFromJwtToken(jwt));
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            log.error("Can NOT set user authentication -> Message", e);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.isBlank(authHeader)) {
            return null;
        }

        if (StringUtils.containsIgnoreCase(authHeader, "Bearer ")) {
            return authHeader.substring(7);
        }

        return StringUtils.replaceIgnoreCase(authHeader, "Bearer ", "", 0);
    }
}
