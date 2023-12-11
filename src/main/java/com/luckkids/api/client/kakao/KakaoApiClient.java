package com.luckkids.api.client.kakao;

import com.luckkids.api.client.OAuthApiClient;
import com.luckkids.api.feign.kakao.KakaoApiFeignCall;
import com.luckkids.api.feign.kakao.KakaoAuthFeignCall;
import com.luckkids.api.feign.kakao.request.KakaoGetTokenRequest;
import com.luckkids.domain.user.SnsType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.client-secret}")
    private String clientSecret;

    private final KakaoAuthFeignCall kakaoAuthFeignCall;
    private final KakaoApiFeignCall kakaoApiFeignCall;

    @Override
    public SnsType oAuthSnsType() {
        return SnsType.KAKAO;
    }

    @Override
    public String getToken(String code) {
        KakaoGetTokenRequest kakaoGetTokenRequest = KakaoGetTokenRequest.builder()
            .client_id(clientId)
            .client_secret(clientSecret)
            .code(code)
            .grant_type(GRANT_TYPE)
            .build();

        return kakaoAuthFeignCall.getToken(kakaoGetTokenRequest).getAccessToken();
    }

    @Override
    public String getEmail(String code){
        return kakaoApiFeignCall.getUserInfo("Bearer " + getToken(code)).getEmail();
    }
}
