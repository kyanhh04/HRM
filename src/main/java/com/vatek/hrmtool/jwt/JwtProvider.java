package com.vatek.hrmtool.jwt;

//import com.vatek.hrmtool.service.serviceImpl.UserPrinciple;
import com.vatek.hrmtool.entity.Config;
import com.vatek.hrmtool.service.serviceImpl.UserOldPrinciple;
import com.vatek.hrmtool.util.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class JwtProvider {

    @Value("${hrm.app.jwtSecret}")
    private String jwtSecret;

    @Value("${hrm.app.jwtExpiration}")
    private int jwtExpiration;

    @Value("${hrm.app.refreshTokenExpiration}")
    private int refreshTokenExpiration;

    public String generateRefreshToken(Authentication authentication){
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserOldPrinciple) {
            UserOldPrinciple userOldPrinciple = (UserOldPrinciple) principal;
            return generateTokenFromUserIdAndRole(userOldPrinciple.getId(), userOldPrinciple.getPositions(), refreshTokenExpiration);
        }
        throw new IllegalArgumentException("Invalid principal type");
    }

    public String generateJwtToken(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserOldPrinciple) {
            UserOldPrinciple userPrincipal = (UserOldPrinciple) principal;
            return generateJwtToken(userPrincipal);
        }
        throw new IllegalArgumentException("Invalid principal type");
    }

    public String generateJwtToken(UserOldPrinciple userPrincipal) {
        // return generateTokenFromUserIdAndRole(userPrincipal.getId(), userPrincipal.getRoles());
        return generateTokenFromUserIdAndRole(userPrincipal.getId(), userPrincipal.getPositions());
    }

    public String generateTokenFromUserIdAndRole(String id, Collection<String> positions) {
        return generateTokenFromUserIdAndRole(id, positions, jwtExpiration);
    }

    public String generateTokenFromUserIdAndRole(String id, Collection<String> positions, int expiration) {

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());


        Map<String,?> claims = Map.of(
                "userId",id,
                "positions",positions
        );

        return Jwts
                .builder()
                .claims(claims)
                .issuedAt(DateUtils.convertInstantToDate(DateUtils.getInstantNow()))
                .expiration(DateUtils.convertInstantToDate(Instant.now().plus(expiration, ChronoUnit.SECONDS)))
                .signWith(key)
                .compact();
    }
    public String getUserIdFromJwtToken(String token) {
        return getSignedClaims(token).getPayload().get("userId",String.class);
    }


    public Long getRemainTimeFromJwtToken(String token) {
        return getSignedClaims(token)
                .getPayload()
                .getExpiration()
                .getTime() - DateUtils.getInstantNow().get(ChronoField.MILLI_OF_SECOND);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            return getRemainTimeFromJwtToken(authToken) > 0;
        } catch (Exception e) {
            log.error("Error validateJwtToken -> Message : ", e);
        }
        return false;
    }

    private Jws<Claims> getSignedClaims(String authToken) {

        SecretKey secret = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.parser().verifyWith(secret).build().parseSignedClaims(authToken);
    }
}
