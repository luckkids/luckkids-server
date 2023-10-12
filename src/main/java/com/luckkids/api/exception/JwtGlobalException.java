package com.luckkids.api.exception;

import lombok.Getter;

@Getter

public class JwtGlobalException extends RuntimeException{

    private ErrorCode errorCode;

    public JwtGlobalException(ErrorCode errorCode, Exception e) {
        super(errorCode.getMessage(), e);
        this.errorCode = errorCode;
    }

    public JwtGlobalException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
