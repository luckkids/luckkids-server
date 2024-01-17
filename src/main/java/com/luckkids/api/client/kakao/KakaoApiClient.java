package com.luckkids.api.client.kakao;

import com.luckkids.api.client.OAuthApiClient;
import com.luckkids.api.feign.kakao.KakaoApiFeignCall;
import com.luckkids.domain.user.SnsType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {

    private final KakaoApiFeignCall kakaoApiFeignCall;

    @Override
    public SnsType oAuthSnsType() {
        return SnsType.KAKAO;
    }

    @Override
    public String getEmail(String accessToken){
        return kakaoApiFeignCall.getUserInfo("Bearer " + accessToken).getEmail();
    }
}
