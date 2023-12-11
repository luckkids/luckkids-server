package com.luckkids.api.client.apple;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckkids.api.client.apple.dto.AppleIdTokenPayload;
import com.luckkids.api.feign.apple.AppleAuthFeignCall;
import com.luckkids.api.feign.apple.request.AppleGetTokenRequest;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.Security;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AppleApiClient {
    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.apple.client-id}")
    private String clientId;

    @Value("${oauth.apple.key-id}")
    private String keyId;

    @Value("${oauth.apple.team-id}")
    private String teamId;

    @Value("${oauth.apple.audience}")
    private String audience;

    @Value("${oauth.apple.private-key}")
    private String privateKey;

    private final AppleAuthFeignCall appleAuthFeignCall;
    private final ObjectMapper objectMapper;
    public String getToken(String code) {
        AppleGetTokenRequest appleGetTokenRequest = AppleGetTokenRequest.builder()
            .client_id(clientId)
            .client_secret(generateClientSecret())
            .code(code)
            .grant_type(GRANT_TYPE)
            .build();

        return appleAuthFeignCall.getToken(appleGetTokenRequest).getIdToken();
    }

    public String getEmail(String code){
        return decodePayload(getToken(code), AppleIdTokenPayload.class).getEmail();
    }

    private String generateClientSecret() {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        return Jwts.builder()
            .setHeaderParam(JwsHeader.KEY_ID, keyId)
            .setIssuer(teamId)
            .setAudience(audience)
            .setSubject(clientId)
            .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
            .setIssuedAt(new Date())
            .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
            .compact();
    }

    private PrivateKey getPrivateKey() {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);

            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
            return converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            throw new RuntimeException("Error converting private key from String", e);
        }
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
