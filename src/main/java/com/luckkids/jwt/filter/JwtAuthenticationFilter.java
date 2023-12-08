package com.luckkids.jwt.filter;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.jwt.JwtTokenProvider;
import com.luckkids.jwt.exception.JwtTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/*
 * Token인증이 필요한 API 메소드 호출이전 Header에 저장되어있는 토큰을 가져와 유효여부를 체크한다.
 * */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final String[] excludePath = {
        "/api/v1/jwt",                  //토큰발급 테스트 API
        "/api/v1/auth",                 //로그인 예정
        "/api/v1/join",                 //회원가입
        "/api/v1/mail",                 //이메일발송
        "/api/v1/user/findEmail",       //비밀번호재발급 전 가입여부 체크
        "/docs",                         //API문서는 예외
        "/health-check",
        "/profile"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            // Header에서 토큰 받아옴
            String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
            // 유효한 토큰인지 확인
            if (token != null && jwtTokenProvider.extractSubject(token)) {
                // 토큰이 유효하면 토큰으로부터 유저 정보를 세팅
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new JwtTokenException(ErrorCode.JWT_UNKNOWN);
            }
        } catch (ExpiredJwtException e) {
            throw new JwtTokenException(ErrorCode.JWT_EXPIRED, e);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtTokenException(ErrorCode.JWT_UNKNOWN, e);
        }

        filterChain.doFilter(request, response);
    }
}
