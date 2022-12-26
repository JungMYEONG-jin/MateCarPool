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

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    @Builder
    public CommonResponse(String message, Integer status) {
        this.message = message;
        this.status = status;
    }
}
