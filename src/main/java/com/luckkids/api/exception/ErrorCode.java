package com.luckkids.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_UNKNOWN( "해당 사용자가 존재하지 않습니다"),
    USER_PASSWORD("아이디 또는 패스워드가 일치하지 않습니다."),
    USER_NORMAL( "이미 가입된 계정입니다."),
    USER_KAKAO( "카카오로그인으로 이미 가입된 계정입니다."),
    USER_GOOGLE( "구글로그인으로 이미 가입된 계정입니다."),
    USER_APPLE( "애플로그인으로 이미 가입된 계정입니다."),

    MAIL_FAIL("이메일 발송에 실패했습니다."),

    //JWT
    JWT_EXPIRED("JWT토큰이 만료되었습니다."),
    JWT_UNKNOWN("JWT토큰이 수신되지 않았거나 형식이 맞지않습니다."),
  
    ALERT_UNKNOWN("존재하지 않는 공지사항입니다."),
  
    FRIEND_UNKNOWN( "친구가 존재하지 않습니다."),

    //Alert Setting
    ALERT_SETTING_UNKNOWN("해당 사용자가 알림설정이 되어있지 않습니다."),

    OAUTH_UNKNOWN("존재하지않는 로그인방식입니다.");

    private final String message;

}
