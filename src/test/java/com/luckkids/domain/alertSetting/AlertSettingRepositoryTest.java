package com.luckkids.domain.alertSetting;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.luckkids.mission.domain.misson.AlertStatus.CHECKED;
import static com.luckkids.domain.user.Role.USER;
import static com.luckkids.domain.user.SettingStatus.COMPLETE;
import static com.luckkids.domain.user.SnsType.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class AlertSettingRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private AlertSettingRepository alertSettingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PushRepository pushRepository;

    @DisplayName("사용자ID와 DeviceID로 사용자의 알림설정을 조회한다.")
    @Test
    void findByPushDeviceIdAndPushUserId() {
        User user = createUser();
        userRepository.save(user);

        Push push = createPush(user);
        pushRepository.save(push);

        AlertSetting alertSetting = createAlertSetting(push);
        alertSettingRepository.save(alertSetting);

        Optional<AlertSetting> savedAlertSetting = alertSettingRepository.findByPushDeviceIdAndPushUserId("testDeviceId", user.getId());

        assertThat(savedAlertSetting)
            .isPresent()
            .hasValueSatisfying(a -> {
                assertThat(a.getPush().getDeviceId()).isEqualTo("testDeviceId");
                assertThat(a.getEntire()).isEqualTo(CHECKED);
                assertThat(a.getMission()).isEqualTo(CHECKED);
                assertThat(a.getLuckMessage()).isEqualTo(CHECKED);
                assertThat(a.getNotice()).isEqualTo(CHECKED);
            });
    }

    @DisplayName("사용자의 알림설정을 삭제한다.")
    @Test
    void deleteAllByUserId() {
        User user = createUser();
        userRepository.save(user);

        Push push = createPush(user);
        pushRepository.save(push);

        AlertSetting alertSetting = createAlertSetting(push);
        AlertSetting savedAlertSetting = alertSettingRepository.save(alertSetting);
        alertSettingRepository.deleteByPushUserId(user.getId());

        Optional<AlertSetting> findAlertSetting = alertSettingRepository.findById(savedAlertSetting.getId());

        assertThat(findAlertSetting.isEmpty()).isTrue();
    }

    private User createUser() {
        return User.builder()
            .email("test@test.com")
            .password("1234")
            .snsType(NORMAL)
            .settingStatus(COMPLETE)
            .missionCount(0)
            .role(USER)
            .luckPhrase("행운입니다.")
            .build();
    }

    private Push createPush(User user){
        return Push.builder()
            .deviceId("testDeviceId")
            .pushToken("testPushToken")
            .user(user)
            .build();
    }

    private AlertSetting createAlertSetting(Push push){
        return AlertSetting.builder()
            .push(push)
            .entire(CHECKED)
            .mission(CHECKED)
            .luckMessage(CHECKED)
            .notice(CHECKED)
            .build();
    }

}
