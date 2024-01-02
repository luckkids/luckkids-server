package com.luckkids.domain.alertSetting;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.luckkids.domain.misson.AlertStatus.CHECKED;
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

    @DisplayName("사용자ID로 사용자의 알림설정을 조회한다.")
    @Test
    void findByUserIdTest(){
        User user = createUser();
        userRepository.save(user);

        AlertSetting alertSetting = createAlertSetting(user);
        alertSettingRepository.save(alertSetting);

        Optional<AlertSetting> savedAlertSetting = alertSettingRepository.findByUserId(user.getId());

        assertThat(savedAlertSetting)
            .isPresent()
            .hasValueSatisfying(a -> {
                assertThat(a.getDeviceId()).isEqualTo("testDevice");
                assertThat(a.getEntire()).isEqualTo(CHECKED);
                assertThat(a.getMission()).isEqualTo(CHECKED);
                assertThat(a.getLuck()).isEqualTo(CHECKED);
                assertThat(a.getNotice()).isEqualTo(CHECKED);
            });
    }

    @DisplayName("사용자ID와 DeviceID로 사용자의 알림설정을 조회한다.")
    @Test
    void findByUserIdAndDeviceId(){
        User user = createUser();
        userRepository.save(user);

        AlertSetting alertSetting = createAlertSetting(user);
        alertSettingRepository.save(alertSetting);

        Optional<AlertSetting> savedAlertSetting = alertSettingRepository.findByUserIdAndDeviceId(user.getId(), "testDevice");

        assertThat(savedAlertSetting)
            .isPresent()
            .hasValueSatisfying(a -> {
                assertThat(a.getDeviceId()).isEqualTo("testDevice");
                assertThat(a.getEntire()).isEqualTo(CHECKED);
                assertThat(a.getMission()).isEqualTo(CHECKED);
                assertThat(a.getLuck()).isEqualTo(CHECKED);
                assertThat(a.getNotice()).isEqualTo(CHECKED);
            });
    }

    @DisplayName("사용자의 알림설정을 삭제한다.")
    @Test
    void deleteAllByUserId(){
        User user = createUser();
        userRepository.save(user);

        AlertSetting alertSetting = createAlertSetting(user);
        AlertSetting savedAlertSetting = alertSettingRepository.save(alertSetting);
        alertSettingRepository.deleteAllByUserId(user.getId());

        Optional<AlertSetting> findAlertSetting = alertSettingRepository.findById(savedAlertSetting.getId());

        assertThat(findAlertSetting.isEmpty()).isTrue();
    }

    private User createUser(){
        return User.builder()
            .email("test@test.com")
            .password("1234")
            .snsType(NORMAL)
            .settingStatus(COMPLETE)
            .missionCount(0)
            .role(USER)
            .luckPhrases("행운입니다.")
            .build();
    }

    private AlertSetting createAlertSetting(User user){
        return AlertSetting.builder()
            .user(user)
            .deviceId("testDevice")
            .entire(CHECKED)
            .mission(CHECKED)
            .luck(CHECKED)
            .notice(CHECKED)
            .build();
    }

}
