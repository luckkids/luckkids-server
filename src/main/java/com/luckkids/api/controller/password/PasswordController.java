package com.luckkids.api.controller.password;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.password.request.UserFindSnsTypeRequest;
import com.luckkids.api.controller.password.request.UserUpdatePasswordRequest;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.user.response.UserFindSnsTypeResponse;
import com.luckkids.api.service.user.response.UserUpdatePasswordResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/password")
public class PasswordController {

    private final UserService userService;
    private final UserReadService userReadService;

    @PatchMapping("/")
    public ApiResponse<UserUpdatePasswordResponse> update(@RequestBody @Valid UserUpdatePasswordRequest userUpdatePasswordRequest){
        return ApiResponse.ok(userService.updatePassword(userUpdatePasswordRequest.toServiceRequest()));
    }

    @GetMapping("/findSnsType")
    public ApiResponse<UserFindSnsTypeResponse> checkEmail(@RequestBody @Valid UserFindSnsTypeRequest userFindSnsTypeRequest){
        return ApiResponse.ok(userReadService.findSnsType(userFindSnsTypeRequest.toServiceRequest()));
    }

}
