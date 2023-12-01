package com.luckkids.api.service.login.request;

import com.luckkids.domain.user.SnsType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthLoginServiceRequest {
    private String code;
    private SnsType snsType;
    private String deviceId;
    private String pushKey;

    @Builder
    private OAuthLoginServiceRequest(String code, SnsType snsType, String deviceId, String pushKey) {
        this.code = code;
        this.snsType = snsType;
        this.deviceId = deviceId;
        this.pushKey = pushKey;
    }
}
