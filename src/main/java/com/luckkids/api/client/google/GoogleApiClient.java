package com.luckkids.api.client.google;

import com.luckkids.api.client.OAuthApiClient;
import com.luckkids.api.feign.google.GoogleApiFeignCall;
import com.luckkids.api.feign.google.GoogleAuthFeignCall;
import com.luckkids.api.feign.google.request.GoogleGetTokenRequest;
import com.luckkids.domain.user.SnsType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class GoogleApiClient implements OAuthApiClient {

    private final GoogleApiFeignCall googleApiFeignCall;

    @Override
    public SnsType oAuthSnsType() {
        return SnsType.GOOGLE;
    }

    @Override
    public String getEmail(String accessToken){
        return googleApiFeignCall.getUserInfo("Bearer " + accessToken).getEmail();
    }
}
