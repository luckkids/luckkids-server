package com.luckkids.api.service.mission;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.mission.request.MissionCreateServiceRequest;
import com.luckkids.api.service.mission.request.MissionUpdateServiceRequest;
import com.luckkids.api.service.mission.response.MissionResponse;
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
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalTime;
import java.util.List;

import static com.luckkids.domain.misson.AlertStatus.CHECKED;
import static com.luckkids.domain.misson.AlertStatus.UNCHECKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class MissionServiceTest extends IntegrationTestSupport {

    @Autowired
    MissionService missionService;

    @Autowired
    MissionReadService missionReadService;

    @Autowired
    MissionRepository missionRepository;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        missionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("미션 내용들을 받아 미션을 생성한다.")
    @Test
    @WithMockUser(username = "1", roles = "USER")
    void createMission() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, "010-1111-1111");
        Mission mission = createMission(user, "운동하기", CHECKED, LocalTime.of(0, 0));
        missionRepository.save(mission);

        MissionCreateServiceRequest request = MissionCreateServiceRequest.builder()
            .missionDescription("책 읽기")
            .alertStatus(CHECKED)
            .alertTime(LocalTime.of(23, 30))
            .build();

        // when
        MissionResponse missionResponse = missionService.createMission(request);

        // then
        assertThat(missionResponse)
            .extracting("missionDescription", "alertStatus", "alertTime")
            .contains("책 읽기", CHECKED, LocalTime.of(23, 30));

        List<Mission> missions = missionRepository.findAll();
        assertThat(missions).hasSize(2)
            .extracting("missionDescription", "alertStatus", "alertTime")
            .containsExactlyInAnyOrder(
                tuple("운동하기", CHECKED, LocalTime.of(0, 0)),
                tuple("책 읽기", CHECKED, LocalTime.of(23, 30))
            );
    }

    @DisplayName("수정할 미션 내용들을 받아 미션을 수정한다.")
    @Test
    void updateMission() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, "010-1111-1111");
        Mission mission = createMission(user, "운동하기", CHECKED, LocalTime.of(0, 0));
        Mission savedMission = missionRepository.save(mission);
        int missionId = savedMission.getId();

        MissionUpdateServiceRequest request = MissionUpdateServiceRequest.builder()
            .missionDescription("책 읽기")
            .alertStatus(CHECKED)
            .alertTime(LocalTime.of(23, 30))
            .build();

        // when
        MissionResponse missionResponse = missionService.updateMission(missionId, request);

        // then
        assertThat(missionResponse)
            .extracting("missionDescription", "alertStatus", "alertTime")
            .contains("책 읽기", CHECKED, LocalTime.of(23, 30));

        List<Mission> missions = missionRepository.findAll();
        assertThat(missions).hasSize(1)
            .extracting("missionDescription", "alertStatus", "alertTime")
            .containsExactlyInAnyOrder(
                tuple("책 읽기", CHECKED, LocalTime.of(23, 30))
            );
    }

    @DisplayName("수정할 미션 내용 하나만 받아 미션을 수정한다.")
    @Test
    void updateOneMission() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, "010-1111-1111");
        Mission mission = createMission(user, "운동하기", UNCHECKED, LocalTime.of(0, 0));
        Mission savedMission = missionRepository.save(mission);
        int missionId = savedMission.getId();

        MissionUpdateServiceRequest request = MissionUpdateServiceRequest.builder()
            .missionDescription("책 읽기")
            .build();

        // when
        MissionResponse missionResponse = missionService.updateMission(missionId, request);

        // then
        assertThat(missionResponse)
            .extracting("missionDescription", "alertStatus", "alertTime")
            .contains("책 읽기", UNCHECKED, LocalTime.of(0, 0));

        List<Mission> missions = missionRepository.findAll();
        assertThat(missions).hasSize(1)
            .extracting("missionDescription", "alertStatus", "alertTime")
            .containsExactlyInAnyOrder(
                tuple("책 읽기", UNCHECKED, LocalTime.of(0, 0))
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