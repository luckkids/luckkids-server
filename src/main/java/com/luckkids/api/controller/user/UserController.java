package com.luckkids.api.controller.user;

import org.springframework.web.bind.annotation.*;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.user.request.UserFindEmailRequest;
import com.luckkids.api.controller.user.request.UserUpdateLuckPhraseRequest;
import com.luckkids.api.controller.user.request.UserUpdateNicknameRequest;
import com.luckkids.api.controller.user.request.UserUpdatePasswordRequest;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.user.response.UserFindSnsTypeResponse;
import com.luckkids.api.service.user.response.UserResponse;
import com.luckkids.api.service.user.response.UserUpdateLuckPhraseResponse;
import com.luckkids.api.service.user.response.UserUpdateNicknameResponse;
import com.luckkids.api.service.user.response.UserUpdatePasswordResponse;
import com.luckkids.api.service.user.response.UserWithdrawResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;
	private final UserReadService userReadService;

	@GetMapping("/me")
	public ApiResponse<UserResponse> getMe() {
		return ApiResponse.ok((userReadService.findByMe()));
	}

	@PatchMapping("/phrase")
	public ApiResponse<UserUpdateLuckPhraseResponse> updatePhrase(
		@RequestBody @Valid UserUpdateLuckPhraseRequest userUpdateLuckPhraseRequest) {
		return ApiResponse.ok(userService.updatePhrase(userUpdateLuckPhraseRequest.toServiceRequest()));
	}

	@PatchMapping("/password")
	public ApiResponse<UserUpdatePasswordResponse> updatePassword(
		@RequestBody @Valid UserUpdatePasswordRequest userUpdatePasswordRequest) {
		return ApiResponse.ok(userService.updatePassword(userUpdatePasswordRequest.toServiceRequest()));
	}

	@PatchMapping("/nickname")
	public ApiResponse<UserUpdateNicknameResponse> updateNickname(
		@RequestBody @Valid UserUpdateNicknameRequest userUpdateNicknameRequest) {
		return ApiResponse.ok(userService.updateNickname(userUpdateNicknameRequest.toServiceRequest()));
	}

	@GetMapping("/findEmail")
	public ApiResponse<UserFindSnsTypeResponse> findEmail(
		@ModelAttribute @Valid UserFindEmailRequest userFindEmailRequest) {
		return ApiResponse.ok(userReadService.findEmail(userFindEmailRequest.toServiceRequest()));
	}

	@DeleteMapping("/withdraw")
	public ApiResponse<UserWithdrawResponse> withdraw() {
		return ApiResponse.ok(userService.withdraw());
	}
}
