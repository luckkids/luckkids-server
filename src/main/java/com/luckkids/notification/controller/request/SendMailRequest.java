package com.luckkids.notification.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendMailRequest {
    private String email;
    private String subject;
    private String text;

    @Builder
    private SendMailRequest(String email, String subject, String text) {
        this.email = email;
        this.subject = subject;
        this.text = text;
    }
}
