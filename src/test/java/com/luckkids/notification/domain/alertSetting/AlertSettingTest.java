package com.luckkids.notification.domain.alertSetting;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.luckkids.domain.user.User;
import com.luckkids.mission.domain.misson.AlertStatus;
import com.luckkids.notification.domain.push.Push;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

public class AlertSettingTest {

	private Object DefaultArbitraryIntrospector;
	FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.build();
	String push = "push";
	String deviceId = "deviceId";

	@Test
	@DisplayName("AlertSetting 도메인의 update메소드를 테스트한다.")
	void updateTest() {
		User user = fixtureMonkey.giveMeOne(User.class);
		AlertSetting setting = fixtureMonkey.giveMeOne(AlertSetting.class);
		System.out.println("setting.getPush() = " + setting.getPush());
		System.out.println("setting.getMission() = " + setting.getMission());
		System.out.println("setting.getNotice() = " + setting.getNotice());
		System.out.println("------------------------------------------------------");
		Push pushObject = fixtureMonkey.giveMeBuilder(Push.class)
			.set("id", 1)
			.set("deviceId", "asdf")
			.set("pushToken", "asdasd")
			.set("sound", "asdasd")
			.set("user", user)
			.set("alertSetting", setting).sample();
		System.out.println("pushObject.getId() = " + pushObject.getId());
		System.out.println("pushObject.getDeviceId() = " + pushObject.getDeviceId());
		System.out.println("==============================================");
		AlertSetting alertSetting = fixtureMonkey.giveMeBuilder(AlertSetting.class)
			.set(push, pushObject)
			.set("luckMessage", AlertStatus.CHECKED)
			.set("notice", AlertStatus.CHECKED)
			.set("mission", AlertStatus.CHECKED)
			.set("entire", AlertStatus.CHECKED)
			.build()
			.sample();
		System.out.println("alertSetting.getPush() = " + alertSetting.getPush());
		System.out.println("alertSetting.getLuckMessage() = " + alertSetting.getLuckMessage());
		System.out.println("alertSetting.getNotice() = " + alertSetting.getNotice());
		System.out.println("alertSetting.getMission() = " + alertSetting.getMission());
		System.out.println("alertSetting.getEntire() = " + alertSetting.getEntire());

		// AlertSetting alertSetting = AlertSetting.builder()
		// 	.push(createPush())
		// 	.luckMessage(AlertStatus.CHECKED)
		// 	.notice(AlertStatus.CHECKED)
		// 	.mission(AlertStatus.CHECKED)
		// 	.entire(AlertStatus.CHECKED)
		// 	.build();

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
