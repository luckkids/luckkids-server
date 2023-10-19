package com.luckkids.api.exception;

import lombok.Getter;

@Getter
public class LuckKidsException extends RuntimeException {

    private final String message;

    public LuckKidsException(ErrorCode errorCode, Exception e) {
        super(errorCode.getMessage(), e);
        this.message = errorCode.getMessage();
    }

    public LuckKidsException(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }

    public LuckKidsException(String message){
        this.message = message;
    }
}
