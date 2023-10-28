package com.luckkids.api.service.missionOutcome;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.missionOutcome.request.MissionOutcomeCreateServiceRequest;
import com.luckkids.domain.missionOutcome.MissionOutcome;
import com.luckkids.domain.missionOutcome.MissionOutcomeRepository;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.luckkids.domain.missionOutcome.MissionStatus.FAILED;
import static com.luckkids.domain.missionOutcome.MissionStatus.SUCCEED;
import static com.luckkids.domain.misson.AlertStatus.UNCHECKED;
import static org.assertj.core.api.Assertions.*;

class MissionOutcomeServiceTest extends IntegrationTestSupport {

    @Autowired
    private MissionOutcomeService missionOutcomeService;

    @Autowired
    private MissionOutcomeRepository missionOutcomeRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        missionOutcomeRepository.deleteAllInBatch();
        missionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("mission을 받아 미션결과 데이터를 생성한다.")
    @Test
    @Transactional
    void createMissionOutcome() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, "010-1111-1111");
        Mission mission = createMission(user, "운동하기", UNCHECKED, LocalTime.of(19, 0));

        MissionOutcomeCreateServiceRequest request = MissionOutcomeCreateServiceRequest.builder()
            .mission(mission)
            .missionDate(LocalDate.of(2023, 10, 25))
            .build();

        // when
        missionOutcomeService.createMissionOutcome(request);

        // then
        List<MissionOutcome> missionOutcomes = missionOutcomeRepository.findAll();

        assertThat(missionOutcomes).hasSize(1)
            .extracting("mission", "missionStatus", "missionDate")
            .containsExactly(tuple(mission, FAILED, LocalDate.of(2023, 10, 25)));
    }


    @DisplayName("업데이트할 id를 받아서 미션결과 상태를 업데이트 한다.")
    @Test
    void updateMissionOutcome() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, "010-1111-1111");
        Mission mission = createMission(user, "운동하기", UNCHECKED, LocalTime.of(19, 0));
        MissionOutcome missionOutcome = createMissionOutcome(mission, LocalDate.of(2023, 10, 26));

        MissionOutcome savedMissionOutcome = missionOutcomeRepository.save(missionOutcome);

        // when
        missionOutcomeService.updateMissionOutcome(savedMissionOutcome.getId(), SUCCEED);

        // then
        List<MissionOutcome> missionOutcomes = missionOutcomeRepository.findAll();

        assertThat(missionOutcomes).hasSize(1)
            .extracting("id", "missionStatus")
            .contains(
                tuple(savedMissionOutcome.getId(), SUCCEED)
            );
    }

    @DisplayName("업데이트할 id를 받아서 미션결과 상태를 업데이트 할 때 id가 없으면 예외가 발생한다.")
    @Test
    void updateMissionOutcomeWithException() {
        // given
        Long missionOutcomeId = 1L;

        // when // then
        assertThatThrownBy(() -> missionOutcomeService.updateMissionOutcome(missionOutcomeId, SUCCEED))
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