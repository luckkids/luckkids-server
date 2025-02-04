package com.luckkids.notification.service.push;

import static com.luckkids.mission.domain.misson.AlertStatus.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SettingStatus;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.mission.domain.misson.AlertStatus;
import com.luckkids.notification.domain.alertSetting.AlertSetting;
import com.luckkids.notification.domain.alertSetting.AlertType;
import com.luckkids.notification.domain.push.Push;
import com.luckkids.notification.domain.push.PushMessage;
import com.luckkids.notification.infra.AlertSettingRepository;
import com.luckkids.notification.infra.PushRepository;
import com.luckkids.notification.service.PushReadService;

public class PushReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private PushRepository pushRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AlertSettingRepository alertSettingRepository;

	@Autowired
	private PushReadService pushReadService;

	@AfterEach
	void tearDown() {
		alertSettingRepository.deleteAllInBatch();
		pushRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("공지사항 알림 동의 Push를 조회한다.")
	@Test
	void findAllNoticeTest() {
		User user = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
		userRepository.save(user);

		IntStream.rangeClosed(1, 10).forEach(i -> {
			Push push = createPush(user, i);
			pushRepository.save(push);

			AlertStatus alertStatus;

			if ((i % 2) == 0)
				alertStatus = CHECKED;
			else
				alertStatus = UNCHECKED;

			alertSettingRepository.save(createAlertSetting(push, alertStatus));
		});

		List<Push> pushList = pushReadService.findAllByAlertType(AlertType.NOTICE);

		assertThat(pushList).extracting("deviceId")
			.containsExactlyInAnyOrder(
				"테스트2",
				"테스트4",
				"테스트6",
				"테스트8",
				"테스트10"
			);
	}

	@DisplayName("미션 알림 동의 Push를 조회한다.")
	@Test
	void findAllMissionTest() {
		User user = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
		userRepository.save(user);

		IntStream.rangeClosed(1, 10).forEach(i -> {
			Push push = createPush(user, i);
			pushRepository.save(push);

			AlertStatus alertStatus;

			if ((i % 2) == 0)
				alertStatus = CHECKED;
			else
				alertStatus = UNCHECKED;

			alertSettingRepository.save(createAlertSetting(push, alertStatus));
		});

		List<Push> pushList = pushReadService.findAllByAlertType(AlertType.MISSION);

		assertThat(pushList).extracting("deviceId")
			.containsExactlyInAnyOrder(
				"테스트2",
				"테스트4",
				"테스트6",
				"테스트8",
				"테스트10"
			);
	}

	@DisplayName("오늘의 한마디 알림 동의 Push를 조회한다.")
	@Test
	void findAllLuckTest() {
		User user = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
		userRepository.save(user);

		IntStream.rangeClosed(1, 10).forEach(i -> {
			Push push = createPush(user, i);
			pushRepository.save(push);

			AlertStatus alertStatus;

			if ((i % 2) == 0)
				alertStatus = CHECKED;
			else
				alertStatus = UNCHECKED;

			alertSettingRepository.save(createAlertSetting(push, alertStatus));
		});

		List<Push> pushList = pushReadService.findAllByAlertType(AlertType.LUCK);

		assertThat(pushList).extracting("deviceId")
			.containsExactlyInAnyOrder(
				"테스트2",
				"테스트4",
				"테스트6",
				"테스트8",
				"테스트10"
			);
	}

	private Push createPush(User user, int i) {
		return Push.builder()
			.user(user)
			.pushToken("testToken")
			.deviceId("테스트" + i)
			.sound(PushMessage.DEFAULT_SOUND.getText())
			.build();
	}

	private User createUser(String email, String password, String nickname, String luckPhrase, int missionCount) {
		return User.builder()
			.email(email)
			.password(password)
			.snsType(SnsType.NORMAL)
			.nickname(nickname)
			.luckPhrase(luckPhrase)
			.role(Role.USER)
			.settingStatus(SettingStatus.COMPLETE)
			.missionCount(missionCount)
			.build();
	}

	private AlertSetting createAlertSetting(Push push, AlertStatus alertStatus) {
		return AlertSetting.builder()
			.push(push)
			.entire(alertStatus)
			.notice(alertStatus)
			.mission(alertStatus)
			.luckMessage(alertStatus)
			.build();
	}

}
