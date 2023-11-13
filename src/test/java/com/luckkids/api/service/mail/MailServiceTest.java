package com.luckkids.api.service.mail;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.mail.request.SendMailServiceRequest;
import com.luckkids.api.service.mail.response.SendMailResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class MailServiceTest extends IntegrationTestSupport {

    @Test
    void SendMailTest() {
        SendMailServiceRequest sendMailServiceRequest = SendMailServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .build();

        given(mailService.sendMail(any(SendMailServiceRequest.class)))
            .willReturn(SendMailResponse.builder()
                .authNum("123456")
                .build()
            );

        SendMailResponse sendMailResponse = mailService.sendMail(sendMailServiceRequest);

        assertThat(sendMailResponse.getAuthNum().length()).isEqualTo(6);
    }

    @Test
    void generateCode() {
        given(mailService.generateCode())
            .willReturn("123456"
            );

        String authNum = mailService.generateCode();

        assertThat(authNum.length()).isEqualTo(6);
    }
}
