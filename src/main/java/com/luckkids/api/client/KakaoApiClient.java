package com.luckkids.api.client;

import com.luckkids.api.client.request.KakaoLoginRequest;
import com.luckkids.api.feign.KakaoFeignCall;
import com.luckkids.api.feign.request.KakaoGetTokenRequest;
import com.luckkids.api.feign.response.KakaoTokenResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

@Component
@RequiredArgsConstructor
public class KakaoApiClient{

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.client-secret}")
    private String clientSecret;

    private final KakaoFeignCall kakaoFeignCall;
    public String getToken(String code) {
        KakaoGetTokenRequest kakaoGetTokenRequest = KakaoGetTokenRequest.builder()
            .client_id(clientId)
            .client_secret(clientSecret)
            .code(code)
            .grant_type(GRANT_TYPE)
            .build();

        KakaoTokenResponse response = kakaoFeignCall.getToken(kakaoGetTokenRequest);
        return response.getAccessToken();
    }
}
