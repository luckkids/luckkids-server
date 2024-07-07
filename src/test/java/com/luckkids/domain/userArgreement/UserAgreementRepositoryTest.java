package com.luckkids.domain.userArgreement;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.domain.userAgreement.AgreementStatus;
import com.luckkids.domain.userAgreement.UserAgreement;
import com.luckkids.domain.userAgreement.UserAgreementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class UserAgreementRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAgreementRepository userAgreementRepository;

    @DisplayName("사용자 약관동의 여부를 저장한다.")
    @Test
    void findByEmail() {
        // given
        String email = "user@daum.net";
        User user = createUser(email, "user1234!", SnsType.KAKAO);
        User savedUser = userRepository.save(user);

        UserAgreement savedAgreement = userAgreementRepository.save(createUserAgreement(savedUser));

        // when
        UserAgreement findUserAgreement = userAgreementRepository.findById(savedAgreement.getId()).get();

        // then
        assertThat(findUserAgreement)
            .extracting(
                    "termUserAgreement",
                    "personalInfoAgreement",
                    "marketingAgreement"
            )
            .contains(
                    AgreementStatus.AGREE,
                    AgreementStatus.AGREE,
                    AgreementStatus.AGREE
            );
    }

    private User createUser(String email, String password, SnsType snsType) {
        return User.builder()
                .email(email)
                .password(password)
                .snsType(snsType)
                .build();
    }

    private UserAgreement createUserAgreement(User user) {
        return UserAgreement.builder()
                .user(user)
                .termUserAgreement(AgreementStatus.AGREE)
                .personalInfoAgreement(AgreementStatus.AGREE)
                .marketingAgreement(AgreementStatus.AGREE)
                .build();
    }
}
