package com.luckkids.domain.missionOutcome;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.luckkids.domain.missionOutcome.MissionStatus.FAILED;
import static com.luckkids.domain.missionOutcome.MissionStatus.SUCCEED;
import static com.luckkids.domain.misson.AlertStatus.UNCHECKED;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class MissionOutcomeRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MissionOutcomeRepository missionOutcomeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MissionRepository missionRepository;

    @DisplayName("유니크로 설정된 missionId와 missionDate를 똑같은 것으로 2번 인서트하려고 할 때 예외가 난다.")
    @Test
    void isUniqueMissionIdAndMissionDate() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
        Mission mission = createMission(user, "운동하기", UNCHECKED, LocalTime.of(19, 0));
        MissionOutcome missionOutcome1 = createMissionOutcome(mission, LocalDate.of(2023, 10, 25), null);
        MissionOutcome missionOutcome2 = createMissionOutcome(mission, LocalDate.of(2023, 10, 25), null);

        userRepository.save(user);
        missionRepository.save(mission);
        missionOutcomeRepository.save(missionOutcome1);

        // when // then
        assertThatThrownBy(() -> missionOutcomeRepository.save(missionOutcome2))
            .isInstanceOf(DataIntegrityViolationException.class);

    }

    @DisplayName("미션 ID와 미션 날짜를 받아서 미션결과를 다 삭제한다.")
    @Test
    void deleteAllByMissionIdAndMissionDate() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
        Mission mission = createMission(user, "운동하기", UNCHECKED, LocalTime.of(19, 0));
        MissionOutcome missionOutcome = createMissionOutcome(mission, LocalDate.of(2023, 10, 25), null);

        userRepository.save(user);
        missionRepository.save(mission);
        missionOutcomeRepository.save(missionOutcome);

        // when
        missionOutcomeRepository.deleteAllByMissionIdAndMissionDate(mission.getId(), LocalDate.of(2023, 10, 25));

        // then
        assertThat(missionOutcomeRepository.findAll()).hasSize(0).isEmpty();
    }

    @DisplayName("사용자의 미션이력을 전부 삭제한다.")
    @Test
    void deleteAllByMissionUserId() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
        Mission mission = createMission(user, "운동하기", UNCHECKED, LocalTime.of(19, 0));
        MissionOutcome missionOutcome1 = createMissionOutcome(mission, LocalDate.of(2023, 10, 25), SUCCEED);
        MissionOutcome missionOutcome2 = createMissionOutcome(mission, LocalDate.of(2023, 10, 26), SUCCEED);
        MissionOutcome missionOutcome3 = createMissionOutcome(mission, LocalDate.of(2023, 10, 27), FAILED);

        userRepository.save(user);
        missionRepository.save(mission);
        missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2, missionOutcome3));

        User user2 = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
        Mission mission2 = createMission(user2, "운동하기", UNCHECKED, LocalTime.of(19, 0));
        MissionOutcome missionOutcome4 = createMissionOutcome(mission2, LocalDate.of(2023, 10, 25), SUCCEED);
        MissionOutcome missionOutcome5 = createMissionOutcome(mission2, LocalDate.of(2023, 10, 26), SUCCEED);
        MissionOutcome missionOutcome6 = createMissionOutcome(mission2, LocalDate.of(2023, 10, 27), FAILED);

        userRepository.save(user2);
        missionRepository.save(mission2);
        missionOutcomeRepository.saveAll(List.of(missionOutcome4, missionOutcome5, missionOutcome6));

        // when
        missionOutcomeRepository.deleteAllByMissionUserId(user.getId());

        List<MissionOutcome> missionOutcomeList = missionOutcomeRepository.findAll();

        // then
        assertThat(missionOutcomeList).hasSize(3);
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
            .missionStatus(ofNullable(missionStatus).orElse(FAILED))
            .build();
    }

}