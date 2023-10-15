package com.luckkids.api.controller.join;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.join.dto.*;
import com.luckkids.api.service.join.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/join")
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/checkEmail")
    public void checkEmail(@RequestBody JoinCheckEmailRequest joinCheckEmailRequest){
        joinService.checkEmail(joinCheckEmailRequest.toServiceRequest());
    }

    @PostMapping("/sendMail")
    public ApiResponse<JoinSendMailResponse> sendEmail(@RequestBody JoinSendMailRequest joinSendMailRequest){
        return ApiResponse.ok(joinService.sendMail(joinSendMailRequest.toServiceRequest()).toControllerResponse());
    }

    @PostMapping("/user")
    public ApiResponse<JoinResponse> joinUser(@RequestBody JoinRequest joinRequest){
        return ApiResponse.ok(joinService.joinUser(joinRequest.toServiceRequest()).toControllerResponse());
    }
}
