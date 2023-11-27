package com.luckkids.api.controller.mail;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.mail.request.MailFindSnsTypeRequest;
import com.luckkids.api.controller.mail.request.SendMailRequest;
import com.luckkids.api.controller.mail.request.SendPasswordRequest;
import com.luckkids.api.service.mail.MailService;
import com.luckkids.api.service.mail.response.SendMailResponse;
import com.luckkids.api.service.mail.response.SendPasswordResponse;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.user.response.UserFindSnsTypeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
public class MailController {

    private final MailService mailService;
    private final UserReadService userReadService;

    @PostMapping("/send")
    public ApiResponse<SendMailResponse> sendEmail(@Valid @RequestBody SendMailRequest sendMailRequest){
        return ApiResponse.ok(mailService.sendMail(sendMailRequest.toServiceRequest()));
    }

    @PostMapping("/password")
    public ApiResponse<SendPasswordResponse> sendPassword(@Valid @RequestBody SendPasswordRequest sendPasswordRequest){
        return ApiResponse.ok(mailService.sendPassword(sendPasswordRequest.toServiceRequest()));
    }

    @GetMapping("/findSnsType")
    public ApiResponse<UserFindSnsTypeResponse> checkEmail(@RequestBody @Valid MailFindSnsTypeRequest mailFindSnsTypeRequest){
        return ApiResponse.ok(userReadService.findSnsType(mailFindSnsTypeRequest.toServiceRequest()));
    }
}
