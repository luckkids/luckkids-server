package com.luckkids.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.jwt.dto.JwtToken;
import com.luckkids.jwt.dto.LoginUserInfo;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 12;             // 12시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 30;      // 30일

    private final JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper objectMapper;

    /*
     * accessToken 발급
     * */
    public JwtToken generate(LoginUserInfo loginUserInfo) throws JsonProcessingException {
        String subject = objectMapper.writeValueAsString(loginUserInfo);
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);                //AccessToken 유효기간 지정
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);                //RefreshToken 유효기간 지정

        String accessToken = jwtTokenProvider.generate(subject, accessTokenExpiredAt);        //AccessToken 생성
        String refreshToken = jwtTokenProvider.generate(subject, refreshTokenExpiredAt);        //RefreshToken 생성

        return JwtToken.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }

    /*
     * refreshtoken으로 access-token재발급
     * */
    public JwtToken generateJwtToken(String refreshToken) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String accessToken = "";

        try {
            if (refreshToken != null && jwtTokenProvider.extractSubject(refreshToken)) {    //RefreshToken 유효여부 체크
                String subject = jwtTokenProvider.getUserPk(refreshToken);                    //Subject 복호화 후 AccessToken으로 생성
                accessToken = jwtTokenProvider.generate(subject, accessTokenExpiredAt);
                refreshToken = jwtTokenProvider.generate(subject, refreshTokenExpiredAt);
            }
        } catch (ExpiredJwtException e) {
            throw new LuckKidsException(ErrorCode.JWT_EXPIRED, e);
        } catch (JwtException | IllegalArgumentException e) {
            throw new LuckKidsException(ErrorCode.JWT_UNKNOWN, e);
        }
        return JwtToken.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }
}
