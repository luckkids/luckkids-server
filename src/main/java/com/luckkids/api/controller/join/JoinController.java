package com.luckkids.api.controller.join;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.join.request.*;
import com.luckkids.api.service.join.JoinReadService;
import com.luckkids.api.service.join.JoinService;
import com.luckkids.api.service.join.response.JoinCheckEmailResponse;
import com.luckkids.api.service.join.response.JoinResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/join")
public class JoinController {

    private final JoinService joinService;
    private final JoinReadService joinReadService;

    @GetMapping("/checkEmail")
    public ApiResponse<JoinCheckEmailResponse> checkEmail(@Valid  @RequestBody JoinCheckEmailRequest joinCheckEmailRequest){
        return ApiResponse.ok(joinReadService.checkEmail(joinCheckEmailRequest.toServiceRequest()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user")
    public ApiResponse<JoinResponse> joinUser(@Valid @RequestBody JoinRequest joinRequest){
        return ApiResponse.created(joinService.joinUser(joinRequest.toServiceRequest()));
    }
}
