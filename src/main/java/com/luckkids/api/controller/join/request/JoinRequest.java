package com.luckkids.api.controller.join.request;

import com.luckkids.api.service.join.request.JoinServiceRequest;
import com.luckkids.domain.userAgreement.AgreementStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
    @NotNull(message = "이용약관 필수동의여부는 필수입니다.")
    private AgreementStatus termUserAgreement;
    @NotNull(message = "개인정보 필수동의여부는 필수입니다.")
    private AgreementStatus personalInfoAgreement;
    @NotNull(message = "마케팅 수신동의여부는 필수입니다.")
    private AgreementStatus marketingAgreement;

    @Builder
    private JoinRequest(String email, String password, AgreementStatus termUserAgreement, AgreementStatus personalInfoAgreement, AgreementStatus marketingAgreement) {
        this.email = email;
        this.password = password;
        this.termUserAgreement = termUserAgreement;
        this.personalInfoAgreement = personalInfoAgreement;
        this.marketingAgreement = marketingAgreement;
    }

    public JoinServiceRequest toServiceRequest(){
        return JoinServiceRequest.builder()
                .email(email)
                .password(password)
                .termUserAgreement(termUserAgreement)
                .personalInfoAgreement(personalInfoAgreement)
                .marketingAgreement(marketingAgreement)
                .build();
    }
}
