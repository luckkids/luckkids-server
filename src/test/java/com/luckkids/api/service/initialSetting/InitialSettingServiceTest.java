package com.luckkids.api.service.initialSetting;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.initialSetting.request.InitialSettingAlertServiceRequest;
import com.luckkids.api.service.initialSetting.request.InitialSettingCharacterServiceRequest;
import com.luckkids.api.service.initialSetting.request.InitialSettingMissionServiceRequest;
import com.luckkids.api.service.initialSetting.request.InitialSettingServiceRequest;
import com.luckkids.api.service.initialSetting.response.InitialSettingAlertResponse;
import com.luckkids.api.service.initialSetting.response.InitialSettingCharacterResponse;
import com.luckkids.api.service.initialSetting.response.InitialSettingMissionResponse;
import com.luckkids.api.service.initialSetting.response.InitialSettingResponse;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.alertSetting.AlertSettingRepository;
import com.luckkids.domain.missionOutcome.MissionOutcome;
import com.luckkids.domain.missionOutcome.MissionOutcomeRepository;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.user.SettingStatus;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.domain.userCharacter.UserCharacterRepository;
import com.luckkids.jwt.dto.UserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.luckkids.domain.misson.AlertStatus.CHECKED;
import static com.luckkids.domain.user.SettingStatus.COMPLETE;
import static com.luckkids.domain.user.SettingStatus.INCOMPLETE;
import static com.luckkids.domain.user.SnsType.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;

public class InitialSettingServiceTest extends IntegrationTestSupport {

    @Autowired
    private InitialSettingService initialSettingService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserReadService userReadService;
    @Autowired
    private AlertSettingRepository alertSettingRepository;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private MissionOutcomeRepository missionOutcomeRepository;
    @Autowired
    private UserCharacterRepository userCharacterRepository;

    @AfterEach
    void tearDown() {
        userCharacterRepository.deleteAllInBatch();
        missionOutcomeRepository.deleteAllInBatch();
        missionRepository.deleteAllInBatch();
        alertSettingRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("사용자의 초기세팅 데이터를 저장한다.")
    @Test
    @Transactional
    void createTest(){
        //given
        User user = createUser(1);

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(user.getId()));

        InitialSettingCharacterServiceRequest initialSettingCharacterServiceRequest = InitialSettingCharacterServiceRequest.builder()
            .characterName("럭키즈")
            .fileName("test.json")
            .build();

        List<InitialSettingMissionServiceRequest> initialSettingMissionServiceRequests = new ArrayList<>();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            initialSettingMissionServiceRequests.add(
                InitialSettingMissionServiceRequest.builder()
                    .missionDescription(i+"시에 운동하기")
                    .alertStatus(CHECKED)
                    .alertTime(LocalTime.of(0,0))
                    .build()
            );
        });

        InitialSettingAlertServiceRequest initialSettingAlertServiceRequest = InitialSettingAlertServiceRequest.builder()
            .alertStatus(CHECKED)
            .build();

        InitialSettingServiceRequest initialSettingServiceRequest = InitialSettingServiceRequest.builder()
            .character(initialSettingCharacterServiceRequest)
            .missions(initialSettingMissionServiceRequests)
            .alertSetting(initialSettingAlertServiceRequest)
            .build();

        //when
        InitialSettingResponse initialSettingResponse = initialSettingService.initialSetting(initialSettingServiceRequest);


        //then
        InitialSettingCharacterResponse initialSettingCharacterResponse = initialSettingResponse.getCharacter();
        List<InitialSettingMissionResponse> initialSettingMissionResponse = initialSettingResponse.getMissions();
        InitialSettingAlertResponse initialSettingAlertResponse = initialSettingResponse.getAlertSetting();

        User savedUser = userReadService.findByOne(user.getId());

        assertThat(savedUser.getSettingStatus()).isEqualTo(COMPLETE);

        assertThat(initialSettingCharacterResponse).extracting("characterName","fileName")
            .containsExactly("럭키즈","test.json");

        assertThat(initialSettingMissionResponse).hasSize(10)
            .extracting("missionDescription","alertStatus","alertTime")
            .containsExactlyInAnyOrder(
                tuple("1시에 운동하기", CHECKED, LocalTime.of(0,0)),
                tuple("2시에 운동하기", CHECKED, LocalTime.of(0,0)),
                tuple("3시에 운동하기", CHECKED, LocalTime.of(0,0)),
                tuple("4시에 운동하기", CHECKED, LocalTime.of(0,0)),
                tuple("5시에 운동하기", CHECKED, LocalTime.of(0,0)),
                tuple("6시에 운동하기", CHECKED, LocalTime.of(0,0)),
                tuple("7시에 운동하기", CHECKED, LocalTime.of(0,0)),
                tuple("8시에 운동하기", CHECKED, LocalTime.of(0,0)),
                tuple("9시에 운동하기", CHECKED, LocalTime.of(0,0)),
                tuple("10시에 운동하기", CHECKED, LocalTime.of(0,0))
            );

        assertThat(initialSettingAlertResponse).extracting("entire","mission","luck","notice")
            .containsExactly(CHECKED, CHECKED, CHECKED, CHECKED);
    }

    private User createUser(int i) {
        return userRepository.save(
            User.builder()
                .email("test"+i)
                .password("password")
                .snsType(NORMAL)
                .settingStatus(INCOMPLETE)
                .build());
    }

    private UserInfo createUserInfo(int userId) {
        return UserInfo.builder()
            .userId(userId)
            .email("")
            .build();
    }
}
