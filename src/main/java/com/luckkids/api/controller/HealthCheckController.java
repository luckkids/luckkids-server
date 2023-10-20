package com.luckkids.api.controller;

import com.luckkids.api.ApiResponse;
import com.luckkids.config.login.LoginUser;
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
    public int test(@LoginUser int userId) {
        return userId;
    }
}
