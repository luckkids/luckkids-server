package com.luckkids.api.controller.user;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.user.request.UserLuckPhrasesRequest;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.user.response.UserLuckPhrasesResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PatchMapping("/phrase")
    public ApiResponse<UserLuckPhrasesResponse> updatePhrase(@RequestBody @Valid UserLuckPhrasesRequest userLuckPhrasesRequest){
        return ApiResponse.ok(userService.updatePhrase(userLuckPhrasesRequest.toServiceRequest()));
    }
}
