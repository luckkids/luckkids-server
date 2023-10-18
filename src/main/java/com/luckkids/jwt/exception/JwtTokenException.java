package com.luckkids.jwt.exception;

import com.luckkids.api.exception.ErrorCode;
import lombok.Getter;

/*
* JwtTokenException은 HttpStatus가 500이 아니라 401로 내려줘야하기에 따로 생성
* */
@Getter
public class JwtTokenException extends RuntimeException{
    private ErrorCode errorCode;

    public JwtTokenException(ErrorCode errorCode, Exception e) {
        super(errorCode.getMessage(), e);
        this.errorCode = errorCode;
    }

    public JwtTokenException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
