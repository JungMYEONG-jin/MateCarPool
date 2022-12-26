package com.example.eunboard.auth.application.port.in;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRefreshDto {
    private String accessToken;
    private String refreshToken;
}
