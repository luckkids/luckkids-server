package com.luckkids.api.service.initialSetting;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
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
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;
import com.luckkids.domain.missionOutcome.MissionOutcomeRepository;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.misson.MissionType;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.domain.userCharacter.UserCharacterRepository;
import com.luckkids.jwt.dto.LoginUserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

public class InitialSettingServiceTest extends IntegrationTestSupport {

    @Autowired
    private InitialSettingService initialSettingService;

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlertSettingRepository alertSettingRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MissionOutcomeRepository missionOutcomeRepository;

    @Autowired
    private UserCharacterRepository userCharacterRepository;

    @Autowired
    private PushRepository pushRepository;

    @Autowired
    private LuckkidsCharacterRepository luckkidsCharacterRepository;

    @AfterEach
    void tearDown() {
        userCharacterRepository.deleteAllInBatch();
        missionOutcomeRepository.deleteAllInBatch();
        missionRepository.deleteAllInBatch();
        alertSettingRepository.deleteAllInBatch();
        pushRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }
    @DisplayName("사용자의 초기세팅 데이터를 저장한다.")
    @Test
    @Transactional
    void createTest() {
        //given
        User user = createUser(1);
        Push push = createPush(user);
        pushRepository.save(push);

        LuckkidsCharacter luckkidsCharacter = luckkidsCharacterRepository.save(createCharacter(CharacterType.SUN));

        given(securityService.getCurrentLoginUserInfo())
            .willReturn(createLoginUserInfo(user.getId()));

        InitialSettingCharacterServiceRequest initialSettingCharacterServiceRequest = InitialSettingCharacterServiceRequest.builder()
            .id(luckkidsCharacter.getId())
            .nickName("럭키즈!!")
            .build();

        List<InitialSettingMissionServiceRequest> initialSettingMissionServiceRequests = new ArrayList<>();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            initialSettingMissionServiceRequests.add(
                InitialSettingMissionServiceRequest.builder()
                    .missionType(MissionType.HEALTH)
                    .missionDescription(i + "시에 운동하기")
                    .alertStatus(CHECKED)
                    .alertTime(LocalTime.of(0, 0))
                    .build()
            );
        });

        InitialSettingAlertServiceRequest initialSettingAlertServiceRequest = InitialSettingAlertServiceRequest.builder()
            .deviceId("testDeviceId")
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

        assertThat(initialSettingCharacterResponse).extracting("lottieFile", "nickName")
            .containsExactly("test.json", "럭키즈!!");

        assertThat(initialSettingMissionResponse).hasSize(10)
            .extracting("missionType", "missionDescription", "alertStatus", "alertTime")
            .containsExactlyInAnyOrder(
                tuple(MissionType.HEALTH, "1시에 운동하기", CHECKED, LocalTime.of(0, 0)),
                tuple(MissionType.HEALTH, "2시에 운동하기", CHECKED, LocalTime.of(0, 0)),
                tuple(MissionType.HEALTH, "3시에 운동하기", CHECKED, LocalTime.of(0, 0)),
                tuple(MissionType.HEALTH, "4시에 운동하기", CHECKED, LocalTime.of(0, 0)),
                tuple(MissionType.HEALTH, "5시에 운동하기", CHECKED, LocalTime.of(0, 0)),
                tuple(MissionType.HEALTH, "6시에 운동하기", CHECKED, LocalTime.of(0, 0)),
                tuple(MissionType.HEALTH, "7시에 운동하기", CHECKED, LocalTime.of(0, 0)),
                tuple(MissionType.HEALTH, "8시에 운동하기", CHECKED, LocalTime.of(0, 0)),
                tuple(MissionType.HEALTH, "9시에 운동하기", CHECKED, LocalTime.of(0, 0)),
                tuple(MissionType.HEALTH, "10시에 운동하기", CHECKED, LocalTime.of(0, 0))
            );

        assertThat(initialSettingAlertResponse).extracting("entire", "mission", "luck", "notice", "luckMessageAlertTime")
            .containsExactly(CHECKED, CHECKED, CHECKED, CHECKED, LocalTime.of(7,0));
    }

    @DisplayName("사용자의 초기세팅이 이미 저장되어 있을 시 예외를 발생시킨다.")
    @Test
    @Transactional
    void createAgainTest() {
        //given
        User user = createUser(1);
        Push push = createPush(user);
        pushRepository.save(push);

        LuckkidsCharacter luckkidsCharacter = luckkidsCharacterRepository.save(createCharacter(CharacterType.SUN));

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user.getId()));

        InitialSettingCharacterServiceRequest initialSettingCharacterServiceRequest = InitialSettingCharacterServiceRequest.builder()
                .id(luckkidsCharacter.getId())
                .nickName("럭키즈!!")
                .build();

        List<InitialSettingMissionServiceRequest> initialSettingMissionServiceRequests = new ArrayList<>();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            initialSettingMissionServiceRequests.add(
                    InitialSettingMissionServiceRequest.builder()
                            .missionType(MissionType.HEALTH)
                            .missionDescription(i + "시에 운동하기")
                            .alertStatus(CHECKED)
                            .alertTime(LocalTime.of(0, 0))
                            .build()
            );
        });

        InitialSettingAlertServiceRequest initialSettingAlertServiceRequest = InitialSettingAlertServiceRequest.builder()
                .deviceId("testDeviceId")
                .alertStatus(CHECKED)
                .build();

        InitialSettingServiceRequest initialSettingServiceRequest = InitialSettingServiceRequest.builder()
                .character(initialSettingCharacterServiceRequest)
                .missions(initialSettingMissionServiceRequests)
                .alertSetting(initialSettingAlertServiceRequest)
                .build();

        initialSettingService.initialSetting(initialSettingServiceRequest);

        //when then
        assertThatThrownBy(() -> initialSettingService.initialSetting(initialSettingServiceRequest))
                .isInstanceOf(LuckKidsException.class)
                .hasMessage("이미 초기세팅이 되어있는 사용자입니다.");
    }

    private User createUser(int i) {
        return userRepository.save(
            User.builder()
                .email("test" + i)
                .password("password")
                .snsType(NORMAL)
                .settingStatus(INCOMPLETE)
                .build());
    }

    private Push createPush(User user) {
        return Push.builder()
            .deviceId("testDeviceId")
            .user(user)
            .pushToken("testPushToken")
            .build();
    }

    private LoginUserInfo createLoginUserInfo(int userId) {
        return LoginUserInfo.builder()
            .userId(userId)
            .build();
    }

    private LuckkidsCharacter createCharacter(CharacterType characterType) {
        return LuckkidsCharacter.builder()
            .characterType(characterType)
            .level(1)
            .lottieFile("test.json")
            .imageFile("test.png")
            .build();
    }
}
