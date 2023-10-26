package com.luckkids.domain.missionOutcome;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.luckkids.domain.missionOutcome.MissionStatus.FAILED;
import static com.luckkids.domain.misson.AlertStatus.UNCHECKED;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class MissionOutcomeRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MissionOutcomeRepository missionOutcomeRepository;

    @DisplayName("유니크로 설정된 missionId와 missionDate를 똑같은 것으로 2번 인서트하려고 할 때 예외가 난다.")
    @Test
    void isUniqueMissionIdAndMissionDate() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, "010-1111-1111");
        Mission mission = createMission(user, "운동하기", UNCHECKED, LocalTime.of(19, 0));
        MissionOutcome missionOutcome1 = createMissionOutcome(mission, LocalDate.of(2023, 10, 25));
        MissionOutcome missionOutcome2 = createMissionOutcome(mission, LocalDate.of(2023, 10, 25));

        missionOutcomeRepository.save(missionOutcome1);

        // when // then
        assertThatThrownBy(() -> missionOutcomeRepository.save(missionOutcome2))
            .isInstanceOf(DataIntegrityViolationException.class);

    }

    private User createUser(String email, String password, SnsType snsType, String phoneNumber) {
        return User.builder()
            .email(email)
            .password(password)
            .snsType(snsType)
            .phoneNumber(phoneNumber)
            .build();
    }

    private Mission createMission(User user, String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        return Mission.builder()
            .user(user)
            .missionDescription(missionDescription)
            .alertStatus(alertStatus)
            .alertTime(alertTime)
            .build();
    }

    private MissionOutcome createMissionOutcome(Mission mission, LocalDate missionDate) {
        return MissionOutcome.builder()
            .mission(mission)
            .missionDate(missionDate)
            .missionStatus(FAILED)
            .build();
    }

}