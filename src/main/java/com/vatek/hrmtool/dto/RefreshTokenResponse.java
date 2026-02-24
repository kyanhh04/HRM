package com.vatek.hrmtool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefreshTokenResponse {
    private String access_token;
    private String refresh_token;
    private String user_id;
    private List<String> positions;
    private long exp;
}
