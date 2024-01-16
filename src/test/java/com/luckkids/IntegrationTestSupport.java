package com.luckkids;

import com.luckkids.api.client.OAuthApiClient;
import com.luckkids.api.client.apple.AppleApiClient;
import com.luckkids.api.client.kakao.KakaoApiClient;
import com.luckkids.api.feign.google.GoogleApiFeignCall;
import com.luckkids.api.feign.kakao.KakaoApiFeignCall;
import com.luckkids.api.service.mail.MailService;
import com.luckkids.api.service.security.SecurityService;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

    @MockBean
    protected SecurityService securityService;

    @MockBean
    protected MailService mailService;

    @MockBean
    protected KakaoApiFeignCall kakaoApiFeignCall;

    @MockBean
    protected GoogleApiFeignCall googleApiFeignCall;
}
