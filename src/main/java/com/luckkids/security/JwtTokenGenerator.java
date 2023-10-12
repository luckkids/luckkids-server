package com.luckkids.security;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.JwtGlobalException;
import com.luckkids.security.dto.JwtToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60;       // 1시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7 * 2;  //2주

    private final JwtTokenProvider jwtTokenProvider;

    public JwtToken generate(String email) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);				//AccessToken 유효기간 지정
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);				//RefreshToken 유효기간 지정

        String accessToken = jwtTokenProvider.generate(email, accessTokenExpiredAt);		//AccessToken 생성
        String refreshToken = jwtTokenProvider.generate(email, refreshTokenExpiredAt);	//RefreshToken 생성

        return JwtToken.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }

    public JwtToken rerfeshAccessToken(String refreshToken) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = "";

        try {
            if (refreshToken != null && jwtTokenProvider.extractSubject(refreshToken)) {	//RefreshToken 유효여부 체크
                String subject = jwtTokenProvider.getUserPk(refreshToken);					//Subject 복호화 후 AccessToken으로 생성
                accessToken = jwtTokenProvider.generate(subject, accessTokenExpiredAt);
            }
        } catch (ExpiredJwtException e) {
            throw new JwtGlobalException(ErrorCode.JWT_EXPIRED, e);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtGlobalException(ErrorCode.JWT_UNKNOWN, e);
        }

        return JwtToken.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }
}
