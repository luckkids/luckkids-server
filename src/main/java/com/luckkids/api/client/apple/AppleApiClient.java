package com.luckkids.api.client.apple;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckkids.api.client.OAuthApiClient;
import com.luckkids.api.client.apple.dto.AppleIdTokenPayload;
import com.luckkids.domain.user.SnsType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AppleApiClient implements OAuthApiClient {
    private final ObjectMapper objectMapper;

    @Override
    public SnsType oAuthSnsType() {
        return SnsType.APPLE;
    }

    @Override
    public String getEmail(String idToken){
        return decodePayload(idToken, AppleIdTokenPayload.class).getEmail();
    }

    private <T> T decodePayload(String token, Class<T> targetClass) {
        String[] tokenParts = token.split("\\.");
        String payloadJWT = tokenParts[1];
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(payloadJWT));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return objectMapper.readValue(payload, targetClass);
        } catch (Exception e) {
            throw new RuntimeException("Error decoding token payload", e);
        }
    }
}
