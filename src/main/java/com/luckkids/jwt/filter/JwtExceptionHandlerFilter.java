package com.luckkids.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckkids.api.ApiResponse;
import com.luckkids.api.exception.ErrorCode;
import com.luckkids.jwt.exception.JwtTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
* Filter Layer에서는 ControllerAdvice가 동작하기 않기 때문에
* Exception처리 Filter를 만들어 실행시킨다.
* */
@Slf4j
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch (JwtTokenException e){
            setErrorResponse(response, e.getErrorCode());
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.addHeader("Content-Type", "application/json; charset=UTF-8");

        try{
            response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.of(HttpStatus.UNAUTHORIZED, errorCode.getMessage(), null)));
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }
}
