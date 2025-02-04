package com.luckkids.notification.controller;

import com.luckkids.api.ApiResponse;
import com.luckkids.notification.controller.request.SendAuthCodeRequest;
import com.luckkids.notification.controller.request.SendPasswordRequest;
import com.luckkids.notification.service.MailService;
import com.luckkids.notification.service.response.SendAuthUrlResponse;
import com.luckkids.notification.service.response.SendPasswordResponse;
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

    @PostMapping("/authUrl")
    public ApiResponse<SendAuthUrlResponse> sendAuthUrl(@Valid @RequestBody SendAuthCodeRequest sendAuthCodeRequest){
        return ApiResponse.ok(mailService.sendAuthUrl(sendAuthCodeRequest.toServiceRequest()));
    }

    @PostMapping("/password")
    public ApiResponse<SendPasswordResponse> sendPassword(@Valid @RequestBody SendPasswordRequest sendPasswordRequest){
        return ApiResponse.ok(mailService.sendPassword(sendPasswordRequest.toServiceRequest()));
    }
}
