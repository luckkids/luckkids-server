package com.luckkids.api.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final String message;

    public NotFoundException(ErrorCode errorCode, Exception e) {
        super(errorCode.getMessage(), e);
        this.message = errorCode.getMessage();
    }

    public NotFoundException(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }

    public NotFoundException(String message){
        this.message = message;
    }
}
