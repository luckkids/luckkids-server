package com.luckkids.domain.userAgreement;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.refreshToken.RefreshToken;
import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SettingStatus;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.userCharacter.Level;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.jwt.dto.JwtToken;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.luckkids.domain.userCharacter.Level.LEVEL_MAX;

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
