package com.luckkids.api.controller.user;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ApiResponse<UserResponse> findByMe() {
		return ApiResponse.ok((userReadService.findByMe()));
	}

	@GetMapping("/{id}")
	public ApiResponse<UserResponse> findById(@PathVariable int id) {
		return ApiResponse.ok((userReadService.findById(id)));
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
