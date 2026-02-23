package com.vatek.hrmtool.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vatek.hrmtool.constant.ResponseConstant;
import com.vatek.hrmtool.response.common.CommonRes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Log4j2
@AllArgsConstructor
public class UnauthorizedHandler implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e)
            throws IOException {

        log.error("Unauthorized error. Message - {}", e.getMessage());

        CommonRes commonRes = CommonRes
                .commonBuilder()
                .code(ResponseConstant.Code.UNAUTHORIZED)
                .message(e.getMessage())
                .build();

        String errorResponseString = objectMapper.writeValueAsString(commonRes);

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(errorResponseString);
    }
}
