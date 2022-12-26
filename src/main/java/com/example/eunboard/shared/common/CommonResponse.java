package com.example.eunboard.shared.common;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonResponse {
    private String message;
    private Integer status;

    @Builder
    public CommonResponse(String message, Integer status) {
        this.message = message;
        this.status = status;
    }
}
