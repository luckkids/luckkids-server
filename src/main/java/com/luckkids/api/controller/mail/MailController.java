package com.luckkids.api.controller.mail;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.mail.request.SendMailRequest;
import com.luckkids.api.service.mail.MailService;
import com.luckkids.api.service.mail.response.SendMailResponse;
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

    @PostMapping("/send")
    public ApiResponse<SendMailResponse> sendEmail(@Valid @RequestBody SendMailRequest sendMailRequest){
        return ApiResponse.ok(mailService.sendMail(sendMailRequest.toServiceRequest()));
    }
}
