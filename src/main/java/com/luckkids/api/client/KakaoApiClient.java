package com.luckkids.api.client;

import com.luckkids.api.feign.kakao.KakaoApiFeignCall;
import com.luckkids.api.feign.kakao.KakaoAuthFeignCall;
import com.luckkids.api.feign.kakao.request.KakaoGetTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoApiClient{

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.client-secret}")
    private String clientSecret;

    private final KakaoAuthFeignCall kakaoAuthFeignCall;
    private final KakaoApiFeignCall kakaoApiFeignCall;
    public String getToken(String code) {
        KakaoGetTokenRequest kakaoGetTokenRequest = KakaoGetTokenRequest.builder()
            .client_id(clientId)
            .client_secret(clientSecret)
            .code(code)
            .grant_type(GRANT_TYPE)
            .build();

        return kakaoAuthFeignCall.getToken(kakaoGetTokenRequest).getAccessToken();
    }

    public String getEmail(String code){
        return kakaoApiFeignCall.getUserInfo("Bearer " + getToken(code)).getEmail();
    }
}
