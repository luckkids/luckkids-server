package com.luckkids.api.controller.password;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.password.request.UserChangePasswordRequest;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.user.response.UserChangePasswordResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/password")
public class PasswordController {

    private final UserService userService;

    @PatchMapping("/update")
    public ApiResponse<UserChangePasswordResponse> changePassword(@RequestBody @Valid UserChangePasswordRequest userChangePasswordRequest){
        return ApiResponse.ok(userService.changePassword(userChangePasswordRequest.toServiceRequest()));
    }

}
