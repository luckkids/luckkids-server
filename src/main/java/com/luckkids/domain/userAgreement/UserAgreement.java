package com.luckkids.domain.userAgreement;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.user.User;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserAgreement extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private AgreementStatus termUserAgreement;

    @Enumerated(EnumType.STRING)
    private AgreementStatus personalInfoAgreement;

    @Enumerated(EnumType.STRING)
    private AgreementStatus marketingAgreement;

    @Builder
    private UserAgreement(int id, User user, AgreementStatus termUserAgreement, AgreementStatus personalInfoAgreement, AgreementStatus marketingAgreement) {
        this.id = id;
        this.user = user;
        this.termUserAgreement = termUserAgreement;
        this.personalInfoAgreement = personalInfoAgreement;
        this.marketingAgreement = marketingAgreement;
    }

}
