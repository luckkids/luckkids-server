package com.luckkids.api.service.alertSetting;

import static com.luckkids.domain.user.SnsType.*;
import static com.luckkids.mission.domain.misson.AlertStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.notification.service.AlertSettingService;
import com.luckkids.notification.service.request.AlertSettingCreateServiceRequest;
import com.luckkids.notification.service.request.AlertSettingLuckMessageAlertTimeServiceRequest;
import com.luckkids.notification.service.request.AlertSettingUpdateServiceRequest;
import com.luckkids.notification.service.response.AlertSettingLuckMessageAlertTimeResponse;
import com.luckkids.notification.service.response.AlertSettingResponse;
import com.luckkids.notification.service.response.AlertSettingUpdateResponse;
import com.luckkids.notification.domain.alertSetting.AlertSetting;
import com.luckkids.notification.infra.AlertSettingRepository;
import com.luckkids.notification.domain.alertSetting.AlertType;
import com.luckkids.notification.domain.push.Push;
import com.luckkids.notification.infra.PushRepository;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.jwt.dto.LoginUserInfo;

public class AlertSettingServiceTest extends IntegrationTestSupport {

	@Autowired
	private AlertSettingService alertSettingService;

	@Autowired
	private AlertSettingRepository alertSettingRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PushRepository pushRepository;

	@AfterEach
	void tearDown() {
		alertSettingRepository.deleteAllInBatch();
		pushRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("사용자의 알림설정을 수정한다.")
	@Test
	void updateAlertSettingTest() {
		User user = createUser();
		Push push = createPush(user);

		pushRepository.save(push);

		createAlertSetting(push);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		AlertSettingUpdateServiceRequest alertSettingUpdateServiceRequest = AlertSettingUpdateServiceRequest.builder()
			.alertStatus(UNCHECKED)
			.alertType(AlertType.LUCK)
			.deviceId("testDeviceId")
			.build();

		AlertSettingUpdateResponse alertSettingUpdateResponse = alertSettingService.updateAlertSetting(
			alertSettingUpdateServiceRequest);

		assertThat(alertSettingUpdateResponse).extracting("entire", "mission", "notice", "friend", "luck")
			.contains(CHECKED, CHECKED, CHECKED, CHECKED, UNCHECKED);
	}

	@DisplayName("사용자의 알림설정을 등록한다.")
	@Test
	void createAlertSettingTest() {
		User user = createUser();
		Push push = createPush(user);

		pushRepository.save(push);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		AlertSettingCreateServiceRequest alertSettingCreateServiceRequest = AlertSettingCreateServiceRequest.builder()
			.deviceId("testDeviceId")
			.alertStatus(UNCHECKED)
			.build();

		AlertSettingResponse alertSettingResponse = alertSettingService.createAlertSetting(
			alertSettingCreateServiceRequest);

		assertThat(alertSettingResponse).extracting("entire", "mission", "notice", "luck", "friend",
				"luckMessageAlertTime")
			.contains(UNCHECKED, UNCHECKED, UNCHECKED, UNCHECKED, UNCHECKED, LocalTime.of(7, 0));
	}

	@DisplayName("사용자의 알림설정을 등록시 사용자가 없다면 예외가 발생한다.")
	@Test
	void createAlertSettingWithNoUser() {
		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(1));

		AlertSettingCreateServiceRequest alertSettingCreateServiceRequest = AlertSettingCreateServiceRequest.builder()
			.alertStatus(UNCHECKED)
			.build();

		assertThatThrownBy(() -> alertSettingService.createAlertSetting(alertSettingCreateServiceRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Push가 존재하지 않습니다.");
	}

	@DisplayName("사용자의 행운의 한마디 알림시간을 수정한다.")
	@Test
	void updateLuckMessageAlertTime() {
		User user = createUser();
		Push push = createPush(user);

		pushRepository.save(push);

		createAlertSetting(push);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		AlertSettingLuckMessageAlertTimeServiceRequest alertSettingLuckMessageAlertTimeRequest = AlertSettingLuckMessageAlertTimeServiceRequest.builder()
			.deviceId("testDeviceId")
			.luckMessageAlertTime(LocalTime.of(8, 0))
			.build();

		AlertSettingLuckMessageAlertTimeResponse alertSettingLuckMessageAlertTimeResponse = alertSettingService.updateLuckMessageAlertTime(
			alertSettingLuckMessageAlertTimeRequest);

		assertThat(alertSettingLuckMessageAlertTimeResponse).extracting("entire", "mission", "notice", "luck", "friend",
				"luckMessageAlertTime")
			.contains(CHECKED, CHECKED, CHECKED, CHECKED, CHECKED, LocalTime.of(8, 0));
	}

	private User createUser() {
		return userRepository.save(
			User.builder()
				.email("test" + 1)
				.password("password")
				.snsType(NORMAL)
				.build());
	}

	private Push createPush(User user) {
		return Push.builder()
			.deviceId("testDeviceId")
			.pushToken("testPushToken")
			.user(user)
			.build();
	}

	private void createAlertSetting(Push push) {
		AlertSetting alertSetting = AlertSetting.builder()
			.push(push)
			.entire(CHECKED)
			.mission(CHECKED)
			.notice(CHECKED)
			.friend(CHECKED)
			.luckMessage(CHECKED)
			.luckMessageAlertTime(LocalTime.of(7, 0))
			.build();

		alertSettingRepository.save(alertSetting);
	}

	private LoginUserInfo createLoginUserInfo(int userId) {
		return LoginUserInfo.builder()
			.userId(userId)
			.build();
	}
}
