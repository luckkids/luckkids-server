package com.luckkids.api.service.missionOutcome;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeResponse;
import com.luckkids.domain.missionOutcome.MissionOutcome;
import com.luckkids.domain.missionOutcome.MissionOutcomeRepository;
import com.luckkids.domain.missionOutcome.MissionStatus;
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
import java.util.List;

import static com.luckkids.domain.missionOutcome.MissionStatus.FAILED;
import static com.luckkids.domain.missionOutcome.MissionStatus.SUCCEED;
import static com.luckkids.domain.misson.AlertStatus.UNCHECKED;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.*;

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
        MissionOutcome missionOutcome = createMissionOutcome(mission, LocalDate.of(2023, 10, 25), FAILED);
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

    @DisplayName("없어도 되는 미션 성공 여부를 받아 미션 결과를 조회하여 응답을 반환한다.")
    @Test
    void getMissionDetailListForStatus() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, "010-1111-1111");
        Mission mission1 = createMission(user, "운동하기", UNCHECKED, LocalTime.of(19, 0));
        Mission mission2 = createMission(user, "책읽기", UNCHECKED, LocalTime.of(20, 0));
        MissionOutcome missionOutcome1 = createMissionOutcome(mission1, LocalDate.of(2023, 10, 25), FAILED);
        MissionOutcome missionOutcome2 = createMissionOutcome(mission2, LocalDate.of(2023, 10, 25), SUCCEED);

        missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2));

        // when
        List<MissionOutcomeResponse> missionOutcomeResponses = missionOutcomeReadService.getMissionDetailListForStatus(empty(), user.getId());

        // then
        assertThat(missionOutcomeResponses).hasSize(2)
            .extracting("missionDescription", "alertTime", "missionStatus")
            .containsExactlyInAnyOrder(
                tuple("운동하기", LocalTime.of(19, 0), FAILED),
                tuple("책읽기", LocalTime.of(20, 0), SUCCEED)
            );
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

    private MissionOutcome createMissionOutcome(Mission mission, LocalDate missionDate, MissionStatus missionStatus) {
        return MissionOutcome.builder()
            .mission(mission)
            .missionDate(missionDate)
            .missionStatus(missionStatus)
            .build();
    }

    private MissionOutcomeResponse createMissionOutcomeResponse(String missionDescription, LocalTime alertTime, MissionStatus missionStatus) {
        return MissionOutcomeResponse.builder()
            .missionDescription(missionDescription)
            .alertTime(alertTime)
            .missionStatus(missionStatus)
            .build();
    }
}