package com.luckkids.api.controller.user;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.user.request.UserFindEmailRequest;
import com.luckkids.api.controller.user.request.UserLuckPhrasesRequest;
import com.luckkids.api.controller.user.request.UserUpdatePasswordRequest;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.user.response.UserFindSnsTypeResponse;
import com.luckkids.api.service.user.response.UserLuckPhrasesResponse;
import com.luckkids.api.service.user.response.UserUpdatePasswordResponse;
import com.luckkids.api.service.user.response.UserWithdrawResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final UserReadService userReadService;

    @PatchMapping("/phrase")
    public ApiResponse<UserLuckPhrasesResponse> updatePhrase(@RequestBody @Valid UserLuckPhrasesRequest userLuckPhrasesRequest){
        return ApiResponse.ok(userService.updatePhrase(userLuckPhrasesRequest.toServiceRequest()));
    }

    @PatchMapping("/password")
    public ApiResponse<UserUpdatePasswordResponse> updatePassword(@RequestBody @Valid UserUpdatePasswordRequest userUpdatePasswordRequest){
        return ApiResponse.ok(userService.updatePassword(userUpdatePasswordRequest.toServiceRequest()));
    }

    @GetMapping("/findEmail")
    public ApiResponse<UserFindSnsTypeResponse> findEmail(@RequestBody @Valid UserFindEmailRequest userFindEmailRequest){
        return ApiResponse.ok(userReadService.findEmail(userFindEmailRequest.toServiceRequest()));
    }

    @DeleteMapping("/withdraw")
    public ApiResponse<UserWithdrawResponse> withdraw(){
        return ApiResponse.ok(userService.withdraw());
    }
}
