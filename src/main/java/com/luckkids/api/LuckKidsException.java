package com.luckkids.api;

import lombok.Getter;

@Getter

public class LuckKidsException extends RuntimeException{

    private ErrorCode errorCode;

    public LuckKidsException(ErrorCode errorCode, Exception e) {
        super(errorCode.getMessage(), e);
        this.errorCode = errorCode;
    }

    public LuckKidsException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
