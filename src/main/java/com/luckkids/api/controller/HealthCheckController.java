package com.luckkids.api.controller;

import com.luckkids.api.ApiResponse;
import com.luckkids.config.login.LoginUser;
import com.luckkids.jwt.dto.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health-check")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.ok("OK");
    }

    /*
     * token 유효 테스트
     * */
    @PostMapping("/jwt/test")
    public UserInfo test(@LoginUser UserInfo userInfo) {
        UserInfo userinfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userInfo;
    }
}
