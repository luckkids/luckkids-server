package com.luckkids.api.service.login.response;

import com.luckkids.domain.user.SettingStatus;
import com.luckkids.domain.user.User;
import com.luckkids.jwt.dto.JwtToken;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthLoginResponse {

    private String email;
    private String accessToken;
    private String refreshToken;
    private SettingStatus settingStatus;

    @Builder
    private OAuthLoginResponse(String email, String accessToken, String refreshToken, SettingStatus settingStatus) {
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.settingStatus = settingStatus;
    }

    public static OAuthLoginResponse of(User user, JwtToken jwtToken){
        return OAuthLoginResponse.builder()
            .email(user.getEmail())
            .accessToken(jwtToken.getAccessToken())
            .refreshToken(jwtToken.getRefreshToken())
            .settingStatus(user.getSettingStatus())
            .build();
    }
}
