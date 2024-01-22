package com.luckkids.domain.alertSetting;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class AlertSettingTest extends IntegrationTestSupport {


    @Test
    @DisplayName("AlertSetting 도메인의 update메소드를 테스트한다.")
    void updateTest() {
        AlertSetting alertSetting = AlertSetting.builder()
            .user(new User())
            .luck(AlertStatus.CHECKED)
            .notice(AlertStatus.CHECKED)
            .mission(AlertStatus.CHECKED)
            .entire(AlertStatus.CHECKED)
            .build();

        alertSetting.update(AlertType.LUCK, AlertStatus.UNCHECKED);

        assertThat(alertSetting).extracting("luck", "notice", "mission", "entire")
            .contains(AlertStatus.UNCHECKED, AlertStatus.CHECKED, AlertStatus.CHECKED, AlertStatus.CHECKED);
    }
}
