package com.luckkids.api.controller;

import com.luckkids.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health-check")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.ok("OK");
    }
}
