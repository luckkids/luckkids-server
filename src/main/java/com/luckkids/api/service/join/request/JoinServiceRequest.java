package com.luckkids.api.service.join.request;

import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SettingStatus;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.userAgreement.AgreementStatus;
import com.luckkids.domain.userAgreement.UserAgreement;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinServiceRequest {

    private String email;
    private String password;
    private AgreementStatus termUserAgreement;
    private AgreementStatus personalInfoAgreement;
    private AgreementStatus marketingAgreement;

    @Builder
    private JoinServiceRequest(String email, String password, AgreementStatus termUserAgreement, AgreementStatus personalInfoAgreement, AgreementStatus marketingAgreement) {
        this.email = email;
        this.password = password;
        this.termUserAgreement = termUserAgreement;
        this.personalInfoAgreement = personalInfoAgreement;
        this.marketingAgreement = marketingAgreement;
    }

    public User toEntity(){
        return User.builder()
            .email(email)
            .password(encryptPassword())
            .missionCount(0)
            .snsType(SnsType.NORMAL)
            .role(Role.USER)
            .settingStatus(SettingStatus.INCOMPLETE)
            .build();
    }

    public UserAgreement toUserAgreementEntity(User user){
        return UserAgreement.builder()
            .user(user)
            .termUserAgreement(termUserAgreement)
            .personalInfoAgreement(personalInfoAgreement)
            .marketingAgreement(marketingAgreement)
            .build();
    }


    public String encryptPassword(){
        return password;
    }
}
