package com.luckkids.api.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    USER_UNKNOWN( "해당 사용자가 존재하지 않습니다"),
    USER_NORMAL( "이미 가입된 계정입니다."),
    USER_KAKAO( "카카오로그인으로 이미 가입된 계정입니다."),
    USER_GOOGLE( "구글로그인으로 이미 가입된 계정입니다."),
    USER_APPLE( "애플로그인으로 이미 가입된 계정입니다."),

    MAIL_FAIL("이메일 발송에 실패했습니다.");

    private String message;

    private ErrorCode(String message) {
        this.message = message;
    }
}
