package com.luckkids.api.controller.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.login.request.LoginGenerateTokenRequest;
import com.luckkids.api.controller.login.request.LoginOauthRequest;
import com.luckkids.api.controller.login.request.LoginRequest;
import com.luckkids.api.service.alertSetting.AlertSettingReadService;
import com.luckkids.api.service.alertSetting.AlertSettingService;
import com.luckkids.api.service.login.LoginService;
import com.luckkids.api.service.login.response.LoginGenerateTokenResponse;
import com.luckkids.api.service.login.response.LoginResponse;
import com.luckkids.api.service.login.response.OAuthLoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class LoginController {

	private final LoginService loginService;

	@PostMapping("/login")
	public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws JsonProcessingException {
		return ApiResponse.ok(loginService.normalLogin(loginRequest.toServiceRequest()));
	}

	@PostMapping("/oauth/login")
	public ApiResponse<OAuthLoginResponse> oauthLogin(@Valid @RequestBody LoginOauthRequest loginOauthRequest) throws JsonProcessingException {
		return ApiResponse.ok(loginService.oauthLogin(loginOauthRequest.toServiceRequest()));
	}

	@PostMapping("/refresh")
	public ApiResponse<LoginGenerateTokenResponse> refreshJwtToken(@Valid @RequestBody LoginGenerateTokenRequest loginGenerateTokenRequest) {
		return ApiResponse.ok(loginService.refreshJwtToken(loginGenerateTokenRequest.toServiceRequest()));
	}
}
