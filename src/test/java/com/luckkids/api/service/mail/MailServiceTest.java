package com.luckkids.api.service.mail;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.mail.request.SendMailServiceRequest;
import com.luckkids.api.service.mail.response.SendMailResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class MailServiceTest extends IntegrationTestSupport {

    @Autowired
    private MailService mailService;

    @Test
    void SendMailTest() {
        SendMailServiceRequest sendMailServiceRequest = SendMailServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .build();

        SendMailResponse sendMailResponse = mailService.sendMail(sendMailServiceRequest);

        assertThat(sendMailResponse.getAuthNum().length()).isEqualTo(6);
    }

    @Test
    void generateCode() {
        String authNum = mailService.generateCode();
        assertThat(authNum.length()).isEqualTo(6);
    }
}
