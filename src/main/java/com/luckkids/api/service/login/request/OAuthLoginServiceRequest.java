package com.luckkids.api.service.login.request;

import com.luckkids.domain.user.SnsType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthLoginServiceRequest {
    private String accessToken;
    private SnsType snsType;
    private String deviceId;
    private String pushKey;

    @Builder
    private OAuthLoginServiceRequest(String accessToken, SnsType snsType, String deviceId, String pushKey) {
        this.accessToken = accessToken;
        this.snsType = snsType;
        this.deviceId = deviceId;
        this.pushKey = pushKey;
    }
}
