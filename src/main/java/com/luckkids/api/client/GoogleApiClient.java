package com.luckkids.api.client;

import com.luckkids.api.feign.google.GoogleApiFeignCall;
import com.luckkids.api.feign.google.GoogleAuthFeignCall;
import com.luckkids.api.feign.google.request.GoogleGetTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class GoogleApiClient {

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.google.client-id}")
    private String clientId;

    @Value("${oauth.google.client-secret}")
    private String clientSecret;

    @Value("${oauth.google.redirect-uri}")
    private String redirect_uri;

    private final GoogleAuthFeignCall googleAuthFeignCall;
    private final GoogleApiFeignCall googleApiFeignCall;
    public String getToken(String code) {
        GoogleGetTokenRequest googleGetTokenRequest = GoogleGetTokenRequest.builder()
            .client_id(clientId)
            .client_secret(clientSecret)
            .code(URLDecoder.decode(code, StandardCharsets.UTF_8))
            .grant_type(GRANT_TYPE)
            .redirect_uri(redirect_uri)
            .build();

        return googleAuthFeignCall.getToken(googleGetTokenRequest).getAccessToken();
    }

    public String getEmail(String code){
        return googleApiFeignCall.getUserInfo("Bearer " + getToken(code)).getEmail();
    }
}
