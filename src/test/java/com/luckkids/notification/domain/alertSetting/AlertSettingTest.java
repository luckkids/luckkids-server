package com.luckkids.notification.domain.alertSetting;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.luckkids.domain.user.User;
import com.luckkids.mission.domain.misson.AlertStatus;
import com.luckkids.notification.domain.push.Push;

public class AlertSettingTest {

	@Test
	@DisplayName("AlertSetting 도메인의 update메소드를 테스트한다.")
	void updateTest() {
		AlertSetting alertSetting = AlertSetting.builder()
			.push(createPush())
			.luckMessage(AlertStatus.CHECKED)
			.notice(AlertStatus.CHECKED)
			.mission(AlertStatus.CHECKED)
			.entire(AlertStatus.CHECKED)
			.build();

		alertSetting.update(AlertType.LUCK, AlertStatus.UNCHECKED);

		assertThat(alertSetting).extracting("luckMessage", "notice", "mission", "entire")
			.contains(AlertStatus.UNCHECKED, AlertStatus.CHECKED, AlertStatus.CHECKED, AlertStatus.CHECKED);
	}

	private Push createPush() {
		return Push.builder()
			.deviceId("testDeviceId")
			.pushToken("testPushToken")
			.user(User.builder().build())
			.build();
	}
}
