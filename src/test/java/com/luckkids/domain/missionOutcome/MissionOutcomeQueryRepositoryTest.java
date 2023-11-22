package com.luckkids.domain.missionOutcome;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.missionOutcome.projection.MissionOutcomeCalenderDto;
import com.luckkids.domain.missionOutcome.projection.MissionOutcomeDetailDto;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.luckkids.domain.missionOutcome.MissionStatus.FAILED;
import static com.luckkids.domain.missionOutcome.MissionStatus.SUCCEED;
import static com.luckkids.domain.misson.AlertStatus.UNCHECKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
class MissionOutcomeQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MissionOutcomeQueryRepository missionOutcomeQueryRepository;

    @Autowired
    private MissionOutcomeRepository missionOutcomeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MissionRepository missionRepository;

    @DisplayName("미션 성공 여부를 받아 미션 결과를 조회한다.")
    @Test
    void findMissionDetailsByStatus1() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
        Mission mission1 = createMission(user, "운동하기", UNCHECKED, LocalTime.of(19, 0));
        Mission mission2 = createMission(user, "책읽기", UNCHECKED, LocalTime.of(20, 0));
        MissionOutcome missionOutcome1 = createMissionOutcome(mission1, LocalDate.of(2023, 10, 25), FAILED);
        MissionOutcome missionOutcome2 = createMissionOutcome(mission2, LocalDate.of(2023, 10, 25), SUCCEED);

        userRepository.save(user);
        missionRepository.saveAll(List.of(mission1, mission2));
        missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2));

        // when
        List<MissionOutcomeDetailDto> missionOutcomeDetailList =
            missionOutcomeQueryRepository.findMissionDetailsByStatus(Optional.empty(), user.getId(), LocalDate.of(2023, 10, 25));

        // then
        assertThat(missionOutcomeDetailList).hasSize(2)
            .extracting("missionDescription", "alertTime", "missionStatus")
            .containsExactlyInAnyOrder(
                tuple("운동하기", LocalTime.of(19, 0), FAILED),
                tuple("책읽기", LocalTime.of(20, 0), SUCCEED)
            );
    }

    @DisplayName("미션 성공 여부를 받아 미션 결과를 조회한다.")
    @Test
    void findMissionDetailsByStatus2() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
        Mission mission1 = createMission(user, "운동하기", UNCHECKED, LocalTime.of(19, 0));
        Mission mission2 = createMission(user, "책읽기", UNCHECKED, LocalTime.of(20, 0));
        MissionOutcome missionOutcome1 = createMissionOutcome(mission1, LocalDate.of(2023, 10, 25), FAILED);
        MissionOutcome missionOutcome2 = createMissionOutcome(mission2, LocalDate.of(2023, 10, 25), SUCCEED);

        userRepository.save(user);
        missionRepository.saveAll(List.of(mission1, mission2));
        missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2));

        // when
        List<MissionOutcomeDetailDto> missionOutcomeDetailList =
            missionOutcomeQueryRepository.findMissionDetailsByStatus(Optional.of(SUCCEED), user.getId(), LocalDate.of(2023, 10, 25));

        // then
        assertThat(missionOutcomeDetailList).hasSize(1)
            .extracting("missionDescription", "alertTime", "missionStatus")
            .contains(tuple("책읽기", LocalTime.of(20, 0), SUCCEED));
    }

    @DisplayName("검색할 범위 날짜를 받아 성공한 미션들이 있는지 조회한다.")
    @Test
    void findMissionOutcomeByDateRangeAndStatus() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
        Mission mission1 = createMission(user, "운동하기", UNCHECKED, LocalTime.of(19, 0));
        Mission mission2 = createMission(user, "책읽기", UNCHECKED, LocalTime.of(20, 0));
        MissionOutcome missionOutcome1 = createMissionOutcome(mission1, LocalDate.of(2023, 11, 25), FAILED);
        MissionOutcome missionOutcome2 = createMissionOutcome(mission2, LocalDate.of(2023, 11, 25), FAILED);
        MissionOutcome missionOutcome3 = createMissionOutcome(mission1, LocalDate.of(2023, 11, 26), SUCCEED);
        MissionOutcome missionOutcome4 = createMissionOutcome(mission2, LocalDate.of(2023, 11, 26), FAILED);

        userRepository.save(user);
        missionRepository.saveAll(List.of(mission1, mission2));
        missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2, missionOutcome3, missionOutcome4));

        // when
        List<MissionOutcomeCalenderDto> missionOutcomes =
            missionOutcomeQueryRepository.findMissionOutcomeByDateRangeAndStatus(
                user.getId(),
                LocalDate.of(2023, 11, 1),
                LocalDate.of(2023, 12, 31)
            );

        // then
        assertThat(missionOutcomes).hasSize(2)
            .extracting("missionDate", "hasSucceed")
            .containsExactlyInAnyOrder(
                tuple(LocalDate.of(2023, 11, 25), false),
                tuple(LocalDate.of(2023, 11, 26), true)
            );

    }

    private User createUser(String email, String password, SnsType snsType) {
        return User.builder()
            .email(email)
            .password(password)
            .snsType(snsType)
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

}