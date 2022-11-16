package com.example.eunboard.shared.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    TICKET_PASS_EXIST(400, "TICKET-PASS-ERR-401", "현재 티켓패스에 탑승중입니다."),
    TICKET_NOT_FOUND(404,"TICKET_NOT_POUND", "티켓 정보를 찾을 수 없습니다."),
    TICKET_IS_EXIST(400, "TICKET_IS_EXIST", "이미 티켓을 생성했습니다."),
    MEMBER_IS_EXIST(400, "MEMBER_IS_EXIST", "이미 존재하는 유저입니다."),
    PHONE_IS_EXIST(400, "PHONE_NUMBER_IS_EXIST", "해당 번호로 가입한 회원이 이미 존재합니다."),
    TICKET_PASS_NOT_FOUND(404, "TICKET-PASS-ERR-404", "티켓패스를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(404, "COMMON-ERR-404", "사용자를 찾을 수 없습니다"),
    LOGIN_INFO_NOT_MATCHED(400, "LOGIN-ERR-400", "아이디 또는 패스워드가 틀립니다."),
    MEMBER_NOT_AUTHORITY(403, "COMMON-ERR-500", "권한이 없습니다."),
    REFRESH_TOKEN_INVALID(498, "REFRESH_TOKEN_INVALID", "The Refresh Token is invalid"),
    TOKEN_INVALID(498, "TOKEN_INVALID", "The Token is invalid");
    private int status;
    private String errorCode;
    private String message;
}
