package com.luckkids.api.controller.join;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.join.dto.JoinCheckEmailRequest;
import com.luckkids.api.controller.join.dto.JoinSendMailRequest;
import com.luckkids.api.controller.join.dto.JoinSendMailResponse;
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

    @PostMapping("sendEmail")
    public ApiResponse<JoinSendMailResponse> sendEmail(@RequestBody JoinSendMailRequest joinSendMailRequest){
        return ApiResponse.ok(joinService.sendMail(joinSendMailRequest.toServiceRequest()).toControllerResponse());
    }
}
