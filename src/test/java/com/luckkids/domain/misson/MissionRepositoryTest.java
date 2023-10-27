package com.luckkids.domain.misson;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

import static com.luckkids.domain.misson.AlertStatus.CHECKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
class MissionRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MissionRepository missionRepository;

    @DisplayName("유저의 아이디로 미션을 조회한다.")
    @Test
    void findAllByUserId() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, "010-1111-1111");
        Mission mission1 = createMission(user, "운동하기", CHECKED, LocalTime.of(19, 0));
        Mission mission2 = createMission(user, "책읽기", CHECKED, LocalTime.of(22, 0));
        missionRepository.saveAll(List.of(mission1, mission2));

        // when
        List<Mission> missions = missionRepository.findAllByUserId(user.getId());

        // then
        assertThat(missions).hasSize(2)
            .extracting("missionDescription", "alertStatus", "alertTime")
            .containsExactlyInAnyOrder(
                tuple("운동하기", CHECKED, LocalTime.of(19, 0)),
                tuple("책읽기", CHECKED, LocalTime.of(22, 0))
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
}