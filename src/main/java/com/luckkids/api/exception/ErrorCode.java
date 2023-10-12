package com.luckkids.api.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    USER_UNKNOWN( "해당 사용자가 존재하지 않습니다");

    private String message;

    private ErrorCode(String message) {
        this.message = message;
    }
}
