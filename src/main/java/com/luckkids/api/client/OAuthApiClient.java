package com.luckkids.api.client;

import com.luckkids.domain.user.SnsType;

public interface OAuthApiClient {
    SnsType oAuthSnsType();
    String getToken(String code);
    String getEmail(String code);
}
