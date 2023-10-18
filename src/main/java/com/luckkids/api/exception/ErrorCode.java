package com.luckkids.api.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    USER_UNKNOWN( "해당 사용자가 존재하지 않습니다"),
    USER_KAKAO( "카카오로그인으로 이미 가입된 계정입니다."),
    USER_GOOGLE( "구글로그인으로 이미 가입된 계정입니다."),
    USER_APPLE( "애플로그인으로 이미 가입된 계정입니다."),
    USER_PASSWORD("아이디 또는 패스워드가 일치하지 않습니다."),
    //JWT
    JWT_EXPIRED("JWT토큰이 만료되었습니다."),
    JWT_UNKNOWN("JWT토큰이 수신되지않았습니다.");

    private final String message;

    private ErrorCode(String message) {
        this.message = message;
    }
}
