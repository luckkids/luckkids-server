package com.luckkids.api.controller.login.request;

import com.luckkids.api.service.login.request.OAuthLoginServiceRequest;
import com.luckkids.domain.user.SnsType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginOauthRequest {

    @NotNull(message = "authorizaion-code는 필수입니다.")
    private String code;
    @NotNull(message = "인증타입은 필수입니다.")
    private SnsType snsType;
    @NotNull(message = "디바이스ID는 필수입니다.")
    private String deviceId;
    @NotNull(message = "푸시토큰은 필수입니다.")
    private String pushKey;


    public OAuthLoginServiceRequest toServiceRequest(){
        return OAuthLoginServiceRequest.builder()
            .code(code)
            .snsType(snsType)
            .deviceId(deviceId)
            .pushKey(pushKey)
            .build();
    }

}
