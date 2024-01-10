package com.luckkids.api.client.google;

import com.luckkids.api.client.OAuthApiClient;
import com.luckkids.api.feign.google.GoogleApiFeignCall;
import com.luckkids.domain.user.SnsType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
