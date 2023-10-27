package com.luckkids.api;

import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.jwt.exception.JwtTokenException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ApiControllerAdvice {

    /**
     * validation Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
            null
        );
    }

    /**
     * 예상치 못한 서버로직에러 발생시 처리
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> exception(Exception e) {
        return ApiResponse.of(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "서버 로직 에러",
            e.getMessage()
        );
    }

    /**
     * 임의로 발생시킨 CustomException처리
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LuckKidsException.class)
    public ApiResponse<Object> LuckKidsException(LuckKidsException e) {
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            e.getMessage(),
            null
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtTokenException.class)
    public ApiResponse<Object> JwtTokenException(JwtTokenException e) {
        return ApiResponse.of(
            HttpStatus.UNAUTHORIZED,
            e.getMessage(),
            null
        );
    }

    /**
     * 존재하지 않는 id Exception
     **/
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Object> NotFoundIdxException(IllegalArgumentException e) {
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            e.getMessage(),
            null
        );
    }

    /**
     * 데이터 제약 조건으로 인한 Exception
     **/
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse<Object> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            "데이터 제약 조건으로 인해 작업에 실패했습니다.",
            null
        );
    }
}
