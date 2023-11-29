package com.luckkids.api.controller.mail;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.mail.request.SendAuthCodeRequest;
import com.luckkids.api.controller.mail.request.SendPasswordRequest;
import com.luckkids.api.service.mail.MailService;
import com.luckkids.api.service.mail.response.SendAuthCodeResponse;
import com.luckkids.api.service.mail.response.SendPasswordResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
public class MailController {

    private final MailService mailService;

    @PostMapping("/authCode")
    public ApiResponse<SendAuthCodeResponse> sendAuthorizationCode(@Valid @RequestBody SendAuthCodeRequest sendAuthCodeRequest){
        return ApiResponse.ok(mailService.sendAuthCode(sendAuthCodeRequest.toServiceRequest()));
    }

    @PostMapping("/password")
    public ApiResponse<SendPasswordResponse> sendPassword(@Valid @RequestBody SendPasswordRequest sendPasswordRequest){
        return ApiResponse.ok(mailService.sendPassword(sendPasswordRequest.toServiceRequest()));
    }
}
