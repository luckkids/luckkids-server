package com.luckkids.api.service.missionOutcome;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.missionOutcome.MissionOutcome;
import com.luckkids.domain.missionOutcome.MissionOutcomeRepository;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.luckkids.domain.missionOutcome.MissionStatus.FAILED;
import static com.luckkids.domain.misson.AlertStatus.UNCHECKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MissionOutcomeReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private MissionOutcomeReadService missionOutcomeReadService;

    @Autowired
    private MissionOutcomeRepository missionOutcomeRepository;

    @AfterEach
    void tearDown() {
        missionOutcomeRepository.deleteAllInBatch();
    }

    @DisplayName("mission의 id를 받아 미션결과를 조회한다.")
    @Test
    @Transactional
    void findByOne() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, "010-1111-1111");
        Mission mission = createMission(user, "운동하기", UNCHECKED, LocalTime.of(19, 0));
        MissionOutcome missionOutcome = createMissionOutcome(mission, LocalDate.of(2023, 10, 25));
        MissionOutcome savedMission = missionOutcomeRepository.save(missionOutcome);

        // when
        MissionOutcome result = missionOutcomeReadService.findByOne(savedMission.getId());

        // then
        assertThat(result).extracting("mission", "missionStatus", "missionDate")
            .containsExactly(mission, FAILED, LocalDate.of(2023, 10, 25));

    }

    @DisplayName("mission의 id를 받아 미션결과를 조회할 때 id가 없는 예외가 발생한다.")
    @Test
    void findByOneWithException() {
        // given
        Long missionOutcomeId = 1L;

        // when // then
        assertThatThrownBy(() -> missionOutcomeReadService.findByOne(missionOutcomeId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 미션결과는 없습니다. id = " + missionOutcomeId);
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