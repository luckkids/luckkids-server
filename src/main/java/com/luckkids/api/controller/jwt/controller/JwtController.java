package com.luckkids.api.controller.jwt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luckkids.api.ApiResponse;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.user.User;
import com.luckkids.jwt.JwtTokenGenerator;
import com.luckkids.jwt.dto.JwtToken;
import com.luckkids.jwt.dto.LoginUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jwt")
public class JwtController {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final UserReadService userReadService;

    @GetMapping("{email}")
    public ApiResponse<JwtToken> getJwtToken(@PathVariable String email) throws JsonProcessingException {
        User user = userReadService.findByEmail(email);
        LoginUserInfo loginUserInfo = LoginUserInfo.builder()
            .userId(user.getId())
            .build();
        return ApiResponse.ok(jwtTokenGenerator.generate(loginUserInfo));
    }
}
