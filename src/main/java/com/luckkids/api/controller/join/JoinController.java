package com.luckkids.api.controller.join;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.join.request.*;
import com.luckkids.api.service.join.JoinService;
import com.luckkids.api.service.join.response.JoinCheckEmailResponse;
import com.luckkids.api.service.join.response.JoinResponse;
import com.luckkids.api.service.join.response.JoinSendMailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/join")
public class JoinController {

    private final JoinService joinService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/checkEmail")
    public ApiResponse<JoinCheckEmailResponse> checkEmail(@Valid  @RequestBody JoinCheckEmailRequest joinCheckEmailRequest){
        return ApiResponse.ok(joinService.checkEmail(joinCheckEmailRequest.toServiceRequest()));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/sendMail")
    public ApiResponse<JoinSendMailResponse> sendEmail(@Valid @RequestBody JoinSendMailRequest joinSendMailRequest){
        return ApiResponse.ok(joinService.sendMail(joinSendMailRequest.toServiceRequest()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user")
    public ApiResponse<JoinResponse> joinUser(@Valid @RequestBody JoinRequest joinRequest){
        return ApiResponse.created(joinService.joinUser(joinRequest.toServiceRequest()));
    }
}
