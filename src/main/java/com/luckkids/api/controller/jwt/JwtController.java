package com.luckkids.api.controller.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luckkids.jwt.JwtTokenGenerator;
import com.luckkids.jwt.dto.JwtToken;
import com.luckkids.jwt.dto.LoginUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jwt")
public class JwtController {

    private final JwtTokenGenerator jwtTokenGenerator;

    /*
     * 토큰 발급 API
     * */
    @PostMapping("/token")
    public JwtToken getToken(@RequestBody LoginUserInfo loginUserInfo) throws JsonProcessingException {
        return jwtTokenGenerator.generate(loginUserInfo);
    }

    /*
     * accesstoken 갱신 API
     * */
    @PostMapping("/refresh")
    public JwtToken refreshToken(String refreshToken) {
        return jwtTokenGenerator.generateAccessToken(refreshToken);
    }

}

