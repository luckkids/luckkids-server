package com.luckkids.api.service.join;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.alertHistory.AlertHistoryReadService;
import com.luckkids.api.service.join.request.JoinCheckEmailServiceRequest;
import com.luckkids.api.service.join.request.JoinServiceRequest;
import com.luckkids.api.service.join.response.JoinCheckEmailResponse;
import com.luckkids.api.service.join.response.JoinResponse;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.alertHistory.AlertDestinationType;
import com.luckkids.domain.alertHistory.AlertHistory;
import com.luckkids.domain.alertHistory.AlertHistoryRepository;
import com.luckkids.domain.alertHistory.AlertHistoryStatus;
import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.domain.userAgreement.AgreementStatus;
import com.luckkids.domain.userAgreement.UserAgreement;
import com.luckkids.domain.userAgreement.UserAgreementRepository;
import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalTime;
import java.util.List;

import static com.luckkids.domain.misson.AlertStatus.CHECKED;
import static com.luckkids.domain.misson.AlertStatus.UNCHECKED;
import static com.luckkids.domain.misson.MissionType.SELF_DEVELOPMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
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

    @Autowired
    private AlertHistoryRepository alertHistoryRepository;

    @AfterEach
    void tearDown() {
        alertHistoryRepository.deleteAllInBatch();
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

        List<AlertHistory> alertHistories = alertHistoryRepository.findByUserId(response.getId());

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

        assertThat(alertHistories).extracting("alertDescription", "alertHistoryStatus", "alertDestinationType")
                .containsExactlyInAnyOrder(
                        tuple("환영해요 :) 럭키즈와 함께 행운을 키워가요!", AlertHistoryStatus.CHECKED, AlertDestinationType.WELCOME)
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
