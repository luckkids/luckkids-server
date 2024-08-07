package com.luckkids.api.service.join;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.join.request.JoinCheckEmailServiceRequest;
import com.luckkids.api.service.join.request.JoinServiceRequest;
import com.luckkids.api.service.join.response.JoinCheckEmailResponse;
import com.luckkids.api.service.join.response.JoinResponse;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.domain.userAgreement.AgreementStatus;
import com.luckkids.domain.userAgreement.UserAgreement;
import com.luckkids.domain.userAgreement.UserAgreementRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class JoinServiceTest extends IntegrationTestSupport {

    @Autowired
    private JoinService joinService;

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAgreementRepository userAgreementRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @AfterEach
    void tearDown() {
        userAgreementRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("회원가입 테스트")
    @Test
    void JoinTest() {
        JoinServiceRequest joinServiceRequest = JoinServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("test1234")
            .termUserAgreement(AgreementStatus.AGREE)
            .personalInfoAgreement(AgreementStatus.AGREE)
            .marketingAgreement(AgreementStatus.AGREE)
            .build();

        JoinResponse response = joinService.joinUser(joinServiceRequest);

        assertThat(response)
            .extracting(
                "email",
                "snsType",
                "termUserAgreement",
                "personalInfoAgreement",
                "marketingAgreement"
            )
            .contains(
                joinServiceRequest.getEmail(),
                SnsType.NORMAL,
                AgreementStatus.AGREE,
                AgreementStatus.AGREE,
                AgreementStatus.AGREE
            );
    }

    @DisplayName("회원가입시 비밀번호가 단방향 암호화가 되었는지 체크한다.")
    @Test
    void JoinTestWithEncryptPassword() {
        JoinServiceRequest joinServiceRequest = JoinServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .termUserAgreement(AgreementStatus.AGREE)
            .personalInfoAgreement(AgreementStatus.AGREE)
            .marketingAgreement(AgreementStatus.AGREE)
            .build();

        JoinResponse response = joinService.joinUser(joinServiceRequest);

        User user = userReadService.findByEmail(response.getEmail());
        String password = user.getPassword();

        assertThat(bCryptPasswordEncoder.matches("1234", password)).isTrue();
    }
}
