package com.luckkids.api.controller.password;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.password.request.UserFindSnsTypeRequest;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.user.response.UserFindSnsTypeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/password")
public class PasswordController {

    private final UserReadService userReadService;

    @GetMapping("/findSnsType")
    public ApiResponse<UserFindSnsTypeResponse> checkEmail(@RequestBody @Valid UserFindSnsTypeRequest userFindSnsTypeRequest){
        return ApiResponse.ok(userReadService.findSnsType(userFindSnsTypeRequest.toServiceRequest()));
    }

}
