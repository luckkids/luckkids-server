package com.luckkids.api;

import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.exception.NotFoundException;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.user.response.UserResponse;
import com.luckkids.jwt.exception.JwtTokenException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiControllerAdvice {

    private final ErrorNotifier errorNotifier; 

    /**
     * 예상치 못한 서버로직에러 발생시 처리
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> exception(Exception e) throws IOException {
        errorNotifier.sendErrorToSlack(e);
        log.error(e.getMessage());
        return ApiResponse.of(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "서버 로직 에러",
            null
        );
    }

    /**
     * validation Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        log.error(e.getMessage());
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
            null
        );
    }

    /**
     * 임의로 발생시킨 CustomException처리
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LuckKidsException.class)
    public ApiResponse<Object> LuckKidsException(LuckKidsException e) {
        log.error(e.getMessage());
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            e.getMessage(),
            null
        );
    }

    /**
     * FeignClient OAuth 호출 오류
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FeignException.FeignClientException.class)
    public ApiResponse<Object> FeignClientException(FeignException.FeignClientException e) {
        log.error(e.getMessage());
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                null
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtTokenException.class)
    public ApiResponse<Object> JwtTokenException(JwtTokenException e) {
        log.error(e.getMessage());
        return ApiResponse.of(
            HttpStatus.UNAUTHORIZED,
            e.getMessage(),
            null
        );
    }

    /**
     * 메서드에 전달된 인자가 부적절할 때 발생하는 예외
     **/
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Object> NotFoundIdxException(IllegalArgumentException e) {
        log.error(e.getMessage());
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
        log.error(e.getMessage());
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            "데이터 제약 조건으로 인해 작업에 실패했습니다.",
            null
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiResponse<Object> notFoundDataException(NotFoundException e) {
        log.error(e.getMessage());
        return ApiResponse.of(
                HttpStatus.NOT_FOUND,
                "데이터가 존재하지 않습니다.",
                null
        );
    }
}
