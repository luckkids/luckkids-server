package com.luckkids.api.service.user;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.user.request.UserLuckPhrasesServiceRequest;
import com.luckkids.api.service.user.request.UserUpdatePasswordServiceRequest;
import com.luckkids.api.service.user.response.UserUpdatePasswordResponse;
import com.luckkids.domain.alertHistory.AlertHistory;
import com.luckkids.domain.alertHistory.AlertHistoryRepository;
import com.luckkids.domain.alertHistory.AlertHistoryStatus;
import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.domain.alertSetting.AlertSettingRepository;
import com.luckkids.domain.friends.Friend;
import com.luckkids.domain.friends.FriendRepository;
import com.luckkids.domain.friends.FriendStatus;
import com.luckkids.domain.missionOutcome.MissionOutcome;
import com.luckkids.domain.missionOutcome.MissionOutcomeRepository;
import com.luckkids.domain.missionOutcome.MissionStatus;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.refreshToken.RefreshToken;
import com.luckkids.domain.refreshToken.RefreshTokenRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.jwt.dto.LoginUserInfo;
import org.junit.jupiter.api.AfterEach;
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
import static com.luckkids.domain.misson.AlertStatus.CHECKED;
import static com.luckkids.domain.misson.AlertStatus.UNCHECKED;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

public class UserServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserReadService userReadService;
    @Autowired
    private AlertSettingRepository alertSettingRepository;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private AlertHistoryRepository alertHistoryRepository;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private MissionOutcomeRepository missionOutcomeRepository;
    @Autowired
    private PushRepository pushRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("사용자 비밀번호를 변경한다.")
    @Test
    void changePasswordTest() {
        User user = createUser("test@email.com", "1234", SnsType.NORMAL);
        userRepository.save(user);

        UserUpdatePasswordServiceRequest userUpdatePasswordServiceRequest = UserUpdatePasswordServiceRequest.builder()
            .email("test@email.com")
            .password("123456")
            .build();

        UserUpdatePasswordResponse userChangePasswordResponse = userService.updatePassword(userUpdatePasswordServiceRequest);

        User findUser = userReadService.findByOne(user.getId());

        assertThat(userChangePasswordResponse.getEmail()).isEqualTo("test@email.com");
        assertThat(user.getPassword()).isNotEqualTo(findUser.getPassword());
    }

    @DisplayName("사용자 비밀번호를 변경시 사용자가 존재하지않으면 예외를 발생시킨다.")
    @Test
    void changePasswordTestThrowException() {
        UserUpdatePasswordServiceRequest userUpdatePasswordServiceRequest = UserUpdatePasswordServiceRequest.builder()
            .email("test@email.com")
            .password("123456")
            .build();

        assertThatThrownBy(() -> userService.updatePassword(userUpdatePasswordServiceRequest))
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("해당 이메일을 사용중인 사용자가 존재하지 않습니다.");
    }

    @DisplayName("사용자의 행운문구를 수정한다.")
    @Test
    @Transactional
    void updatePhraseTest() {
        User user = createUser("test@email.com", "1234", SnsType.NORMAL);
        userRepository.save(user);
        given(securityService.getCurrentLoginUserInfo())
            .willReturn(createLoginUserInfo(user.getId()));

        UserLuckPhrasesServiceRequest userLuckPhrasesServiceRequest = UserLuckPhrasesServiceRequest.builder()
            .luckPhrases("행운입니다.")
            .build();

        userService.updatePhrase(userLuckPhrasesServiceRequest);

        User savedUser = userReadService.findByOne(user.getId());

        assertThat(savedUser).extracting("email", "snsType", "luckPhrases")
            .contains("test@email.com", SnsType.NORMAL, "행운입니다.");
    }

    @DisplayName("사용자의 모든 데이터를 삭제한다.")
    @Test
    @Transactional
    void withdrawUser() {
        //given user
        User user = User.builder()
            .email("test@email.com")
            .password("1234")
            .snsType(SnsType.NORMAL)
            .build();

        User user2 = User.builder()
            .email("test2@email.com")
            .password("12345")
            .snsType(SnsType.NORMAL)
            .build();

        userRepository.save(user);
        userRepository.save(user2);

        given(securityService.getCurrentLoginUserInfo())
            .willReturn(createLoginUserInfo(user.getId()));

        //given alertHistory
        AlertHistory alertHistory = AlertHistory.builder()
            .user(user)
            .alertDescription("test")
            .alertHistoryStatus(AlertHistoryStatus.CHECKED)
            .build();

        AlertHistory savedAlertHistory = alertHistoryRepository.save(alertHistory);

        //given alertSetting
        AlertSetting alertSetting = AlertSetting.builder()
            .user(user)
            .deviceId("testDevice")
            .entire(CHECKED)
            .mission(CHECKED)
            .luck(CHECKED)
            .notice(CHECKED)
            .build();

        AlertSetting savedAlertSetting = alertSettingRepository.save(alertSetting);

        //given friend
        Friend friend = Friend.builder()
            .requester(user)
            .receiver(user2)
            .friendStatus(FriendStatus.ACCEPTED)
            .build();

        Friend friend2 = Friend.builder()
            .requester(user2)
            .receiver(user)
            .friendStatus(FriendStatus.REQUESTED)
            .build();

        friendRepository.save(friend);
        friendRepository.save(friend2);

        //given mission
        Mission mission = createMission(user, "운동하기", UNCHECKED, LocalTime.of(19, 0));
        MissionOutcome missionOutcome1 = createMissionOutcome(mission, LocalDate.of(2023, 10, 25), SUCCEED);
        MissionOutcome missionOutcome2 = createMissionOutcome(mission, LocalDate.of(2023, 10, 26), SUCCEED);
        MissionOutcome missionOutcome3 = createMissionOutcome(mission, LocalDate.of(2023, 10, 27), FAILED);

        Mission savedMission = missionRepository.save(mission);
        missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2, missionOutcome3));

        //given push
        Push push = Push.builder()
            .deviceId("testDevice")
            .pushToken("testPushToken")
            .user(user)
            .build();

        Push savedPush = pushRepository.save(push);

        //given refreshToken
        RefreshToken token = RefreshToken.builder()
            .deviceId("testDevice")
            .refreshToken("testRefreshToken")
            .user(user)
            .build();

        RefreshToken savedToken = refreshTokenRepository.save(token);

        //when
        userService.withdraw();

        //then
        Optional<AlertHistory> findAlertHistory = alertHistoryRepository.findById(savedAlertHistory.getId());
        Optional<AlertSetting> findAlertSetting = alertSettingRepository.findById(savedAlertSetting.getId());
        List<Friend> friendList = friendRepository.findAll();
        Optional<Mission> findMission = missionRepository.findById(savedMission.getId());
        List<MissionOutcome> missionOutcomeList = missionOutcomeRepository.findAll();
        Optional<Push> findPush = pushRepository.findById(savedPush.getId());
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findById(savedToken.getId());

        assertThat(findAlertHistory.isEmpty()).isTrue();
        assertThat(findAlertSetting.isEmpty()).isTrue();
        assertThat(friendList).hasSize(0);
        assertThat(findMission.isEmpty()).isTrue();
        assertThat(missionOutcomeList).hasSize(0);
        assertThat(findPush.isEmpty()).isTrue();
        assertThat(findRefreshToken.isEmpty()).isTrue();
    }

    private User createUser(String email, String password, SnsType snsType) {
        return User.builder()
            .email(email)
            .password(password)
            .snsType(snsType)
            .build();
    }

    private LoginUserInfo createLoginUserInfo(int userId) {
        return LoginUserInfo.builder()
            .userId(userId)
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
