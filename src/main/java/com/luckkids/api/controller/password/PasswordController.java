package com.luckkids.api.controller.password;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.password.request.UserChangePasswordRequest;
import com.luckkids.api.controller.password.request.UserFindSnsTypeRequest;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.user.response.UserChangePasswordResponse;
import com.luckkids.api.service.user.response.UserFindSnsTypeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/password")
public class PasswordController {

    private final UserService userService;
    private final UserReadService userReadService;

    @PatchMapping("/update")
    public ApiResponse<UserChangePasswordResponse> update(@RequestBody @Valid UserChangePasswordRequest userChangePasswordRequest){
        return ApiResponse.ok(userService.changePassword(userChangePasswordRequest.toServiceRequest()));
    }

    @GetMapping("/checkEmail")
    public ApiResponse<UserFindSnsTypeResponse> checkEmail(@RequestBody @Valid UserFindSnsTypeRequest userFindSnsTypeRequest){
        return ApiResponse.ok(userReadService.findSnsType(userFindSnsTypeRequest.toServiceRequest()));
    }

}
