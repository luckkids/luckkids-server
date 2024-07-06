package com.luckkids.api.service.join.response;

import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.userAgreement.AgreementStatus;
import com.luckkids.domain.userAgreement.UserAgreement;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JoinResponse {

    private int id;
    private String email;
    private SnsType snsType;
    private AgreementStatus termUserAgreement;
    private AgreementStatus personalInfoAgreement;
    private AgreementStatus marketingAgreement;

    @Builder
    private JoinResponse(int id, String email, SnsType snsType, AgreementStatus termUserAgreement, AgreementStatus personalInfoAgreement, AgreementStatus marketingAgreement) {
        this.id = id;
        this.email = email;
        this.snsType = snsType;
        this.termUserAgreement = termUserAgreement;
        this.personalInfoAgreement = personalInfoAgreement;
        this.marketingAgreement = marketingAgreement;
    }

    public static JoinResponse of(User user, UserAgreement userAgreement){
        return JoinResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .snsType(user.getSnsType())
            .termUserAgreement(userAgreement.getTermUserAgreement())
            .personalInfoAgreement(userAgreement.getPersonalInfoAgreement())
            .marketingAgreement(userAgreement.getMarketingAgreement())
            .build();
    }
}
