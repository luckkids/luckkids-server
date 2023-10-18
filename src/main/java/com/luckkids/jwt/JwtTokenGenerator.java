package com.luckkids.jwt;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.jwt.dto.JwtToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 12;       // 12시간 -> 테스트 용이하게 12시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;      // 24시간 -> 테스트 용이하게 24시간

    private final JwtTokenProvider jwtTokenProvider;

    /*
    * accessToken 발급
    * */
    public JwtToken generate(String email) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);				//AccessToken 유효기간 지정
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);				//RefreshToken 유효기간 지정

        String accessToken = jwtTokenProvider.generate(email, accessTokenExpiredAt);		//AccessToken 생성
        String refreshToken = jwtTokenProvider.generate(email, refreshTokenExpiredAt);	    //RefreshToken 생성

        return JwtToken.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }

    /*
    * refreshtoken으로 access-token재발급
    * */
    public JwtToken generateAccessToken(String refreshToken) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = "";

        try {
            if (refreshToken != null && jwtTokenProvider.extractSubject(refreshToken)) {	//RefreshToken 유효여부 체크
                String subject = jwtTokenProvider.getUserPk(refreshToken);					//Subject 복호화 후 AccessToken으로 생성
                accessToken = jwtTokenProvider.generate(subject, accessTokenExpiredAt);
            }
        } catch (ExpiredJwtException e) {
            throw new LuckKidsException(ErrorCode.JWT_EXPIRED, e);
        } catch (JwtException | IllegalArgumentException e) {
            throw new LuckKidsException(ErrorCode.JWT_UNKNOWN, e);
        }

        return JwtToken.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }
}
