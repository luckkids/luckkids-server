package com.luckkids.api.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    USER_UNKNOWN( "해당 사용자가 존재하지 않습니다"),
    //JWT
    JWT_EXPIRED("JWT토큰이 만료되었습니다."),
    JWT_UNKNOWN("JWT토큰이 수신되지 않았거나 형식이 맞지않습니다.");

    private final String message;

    private ErrorCode(String message) {
        this.message = message;
    }
}
