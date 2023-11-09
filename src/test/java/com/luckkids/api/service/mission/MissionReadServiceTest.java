package com.luckkids.api.service.mission;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.mission.MissionReadService;
import com.luckkids.api.service.mission.response.MissionResponse;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.jwt.dto.UserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.List;

import static com.luckkids.domain.misson.AlertStatus.CHECKED;
import static com.luckkids.domain.misson.AlertStatus.UNCHECKED;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class MissionReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private MissionReadService missionReadService;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        missionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("로그인 된 유저(유저1)의 미션들을 가져온다.")
    @Test
    void getMission1() {
        // given
        User user1 = createUser("user1@daum.net", "user1234!", SnsType.KAKAO, "010-1111-1111");
        User user2 = createUser("user2@daum.net", "user1234!", SnsType.KAKAO, "010-1111-1111");
        Mission mission1_1 = createMission(user1, "운동하기", UNCHECKED, LocalTime.of(19, 0));
        Mission mission2_1 = createMission(user2, "책읽기", CHECKED, LocalTime.of(13, 0));
        Mission mission2_2 = createMission(user2, "공부하기", UNCHECKED, LocalTime.of(23, 0));

        userRepository.saveAll(List.of(user1, user2));
        missionRepository.saveAll(List.of(mission1_1, mission2_1, mission2_2));

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(user1.getId()));

        // when
        List<MissionResponse> missions = missionReadService.getMission();

        // then
        assertThat(missions).extracting("missionDescription", "alertStatus", "alertTime")
            .containsExactlyInAnyOrder(
                tuple("운동하기", UNCHECKED, LocalTime.of(19, 0))
            );

    }

    @DisplayName("로그인 된 유저(유저2)의 미션들을 가져온다.")
    @Test
    void getMission2() {
        // given
        User user1 = createUser("user1@daum.net", "user1234!", SnsType.KAKAO, "010-1111-1111");
        User user2 = createUser("user2@daum.net", "user1234!", SnsType.KAKAO, "010-1111-1111");
        Mission mission1_1 = createMission(user1, "운동하기", UNCHECKED, LocalTime.of(19, 0));
        Mission mission2_1 = createMission(user2, "책읽기", CHECKED, LocalTime.of(13, 0));
        Mission mission2_2 = createMission(user2, "공부하기", UNCHECKED, LocalTime.of(23, 0));

        userRepository.saveAll(List.of(user1, user2));
        missionRepository.saveAll(List.of(mission1_1, mission2_1, mission2_2));

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(user2.getId()));

        // when
        List<MissionResponse> missions = missionReadService.getMission();

        // then
        assertThat(missions).extracting("missionDescription", "alertStatus", "alertTime")
            .containsExactlyInAnyOrder(
                tuple("책읽기", CHECKED, LocalTime.of(13, 0)),
                tuple("공부하기", UNCHECKED, LocalTime.of(23, 0))
            );

    }

    @DisplayName("mission의 id를 받아 조회한다.")
    @Test
    void findByOne() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, "010-1111-1111");
        Mission mission = createMission(user, "운동하기", UNCHECKED, LocalTime.of(19, 0));

        userRepository.save(user);
        Mission savedMission = missionRepository.save(mission);

        // when
        Mission result = missionReadService.findByOne(savedMission.getId());

        // then
        assertThat(result).extracting("missionDescription", "alertStatus", "alertTime")
            .containsExactly("운동하기", UNCHECKED, LocalTime.of(19, 0));

    }

    @DisplayName("mission의 id를 받아 조회할 때 id가 없는 예외가 발생한다.")
    @Test
    void findByOneWithException() {
        // given
        int missionId = 1;

        // when // then
        assertThatThrownBy(() -> missionReadService.findByOne(missionId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 미션은 없습니다. id = " + missionId);
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

    private UserInfo createUserInfo(int userId) {
        return UserInfo.builder()
            .userId(userId)
            .email("")
            .build();
    }
}