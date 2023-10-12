package com.luckkids.api.controller.login;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.login.dto.LoginOauthRequest;
import com.luckkids.api.controller.login.dto.LoginRequest;
import com.luckkids.api.controller.login.dto.LoginResponse;
import com.luckkids.api.service.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;

	/*
	* 일반로그인
	* */
	@PostMapping("/login")
	public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest){
		LoginResponse loginResponse = loginService.login(loginRequest.toServiceRequest()).toRequest();
		return ApiResponse.ok(loginResponse);
	}

	@PostMapping("/oauth/login")
	public void oauthLogin(@RequestBody LoginOauthRequest loginOauthRequest){

	}

}
