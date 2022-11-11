package com.example.eunboard.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    TICKET_PASS_EXIST(401, "TICKET-PASS-ERR-401", "현재 티켓패스에 탑승중입니다."),
    TICKET_NOT_FOUND(404,"TICKET_NOT_POUND", "티켓 정보를 찾을 수 없습니다."),
    TICKET_IS_EXIST(401, "TICKET_IS_EXIST", "이미 티켓을 생성했습니다."),
    TICKET_PASS_NOT_FOUND(404, "TICKET-PASS-ERR-404", "티켓패스를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(404, "COMMON-ERR-404", "사용자를 찾을 수 없습니다"),
    MEMBER_NOT_AUTHORITY(500, "COMMON-ERR-500", "권한이 없습니다.");

    private int status;
    private String errorCode;
    private String message;
}
