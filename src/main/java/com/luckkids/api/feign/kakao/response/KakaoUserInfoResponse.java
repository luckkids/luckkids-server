package com.luckkids.api.feign.kakao.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoResponse {

    private Long id;

    @JsonProperty("connected_at")
    private String connectedAt;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    public static class KakaoAccount {

        @JsonProperty("has_email")
        private boolean hasEmail;

        @JsonProperty("email_needs_agreement")
        private boolean emailNeedsAgreement;

        @JsonProperty("is_email_valid")
        private boolean isEmailValid;

        @JsonProperty("is_email_verified")
        private boolean isEmailVerified;

        @JsonProperty("email")
        private String email;

        @Builder
        private KakaoAccount(boolean hasEmail, boolean emailNeedsAgreement, boolean isEmailValid, boolean isEmailVerified, String email) {
            this.hasEmail = hasEmail;
            this.emailNeedsAgreement = emailNeedsAgreement;
            this.isEmailValid = isEmailValid;
            this.isEmailVerified = isEmailVerified;
            this.email = email;
        }
    }

    @Builder
    private KakaoUserInfoResponse(long id, String connectedAt, KakaoAccount kakaoAccount) {
        this.id = id;
        this.connectedAt = connectedAt;
        this.kakaoAccount = kakaoAccount;
    }

    public String getEmail() {
        return kakaoAccount.email;
    }
}