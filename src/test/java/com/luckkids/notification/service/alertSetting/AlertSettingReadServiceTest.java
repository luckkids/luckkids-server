package com.luckkids.notification.service.alertSetting;

import static com.luckkids.domain.user.SnsType.*;
import static com.luckkids.mission.domain.misson.AlertStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.jwt.dto.LoginUserInfo;
import com.luckkids.notification.domain.alertSetting.AlertSetting;
import com.luckkids.notification.domain.push.Push;
import com.luckkids.notification.infra.AlertSettingRepository;
import com.luckkids.notification.infra.PushRepository;
import com.luckkids.notification.service.AlertSettingReadService;
import com.luckkids.notification.service.request.AlertSettingServiceRequest;
import com.luckkids.notification.service.response.AlertSettingResponse;

public class AlertSettingReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private AlertSettingReadService alertSettingReadService;

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

	@DisplayName("AlertSetting Entity를 조회한다.")
	@Test
	void findOneByUserId() {
		User user = createUser(1);
		Push push = createPush(user);
		createAlertSetting(push);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		AlertSetting alertSetting = alertSettingReadService.findOneByDeviceIdAndUserId("testDeviceId");

		assertThat(alertSetting).extracting("entire", "mission", "notice", "friend", "luckMessage")
			.contains(CHECKED, CHECKED, CHECKED, CHECKED, CHECKED);
	}

	@DisplayName("AlertSetting Entity를 조회시 존재하지 않으면 예외가 발생된다.")
	@Test
	void findOneByUserIdWithNoUser() {
		User user = createUser(1);
		Push push = createPush(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		assertThatThrownBy(() -> alertSettingReadService.findOneByDeviceIdAndUserId("testDeviceId"))
			.isInstanceOf(LuckKidsException.class)
			.hasMessage("해당 사용자가 알림설정이 되어있지 않습니다.");
	}

	@DisplayName("userId와 deviceId로 AlertSetting Entity를 조회한다.")
	@Test
	void findOneByUserIdAndDeviceId() {
		User user = createUser(1);
		Push push = createPush(user);
		createAlertSetting(push);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		AlertSetting alertSetting = alertSettingReadService.findOneByDeviceIdAndUserId("testDeviceId");

		assertThat(alertSetting).extracting("entire", "mission", "notice", "friend", "luckMessage")
			.contains(CHECKED, CHECKED, CHECKED, CHECKED, CHECKED);
	}

	@DisplayName("userId와 deviceId로 AlertSetting Entity를 조회시 존재하지 않으면 예외가 발생된다.")
	@Test
	void findOneByUserIdAndDeviceIdWithNoUser() {
		//        given(securityService.getCurrentLoginUserInfo())
		//            .willReturn(createLoginUserInfo(1));
		//
		//        assertThatThrownBy(() -> alertSettingReadService.findOneByUserIdAndDeviceId("testDeviceId"))
		//            .isInstanceOf(LuckKidsException.class)
		//            .hasMessage("해당 사용자가 알림설정이 되어있지 않습니다.");
	}

	@DisplayName("사용자의 알림세팅을 조회한다.")
	@Test
	void getAlertSetting() {
		User user = createUser(1);
		Push push = createPush(user);
		createAlertSetting(push);

		AlertSettingServiceRequest request = AlertSettingServiceRequest.builder()
			.deviceId("testDeviceId")
			.build();

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		AlertSettingResponse alertSettingResponse = alertSettingReadService.getAlertSetting(request);

		assertThat(alertSettingResponse).extracting("entire", "mission", "notice", "friend", "luck")
			.contains(CHECKED, CHECKED, CHECKED, CHECKED, CHECKED);
	}

	@DisplayName("사용자의 기기가 아닌 DeviceId로 알림세팅을 조회시 예외가 발생한다.")
	@Test
	void getAlertSettingAnotherDeviceId() {
		User user = createUser(1);
		Push push = createPush(user);
		createAlertSetting(push);

		AlertSettingServiceRequest request = AlertSettingServiceRequest.builder()
			.deviceId("testDevice")
			.build();

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		assertThatThrownBy(() -> alertSettingReadService.getAlertSetting(request))
			.isInstanceOf(LuckKidsException.class)
			.hasMessage("해당 사용자가 알림설정이 되어있지 않습니다.");
	}

	@DisplayName("사용자의 알림세팅이 존재하지 않을 시 예외가 발생한다.")
	@Test
	void getAlertSettingWithNoSetting() {
		User user = createUser(1);
		Push push = createPush(user);

		AlertSettingServiceRequest request = AlertSettingServiceRequest.builder()
			.deviceId("testDeviceId")
			.build();

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		assertThatThrownBy(() -> alertSettingReadService.getAlertSetting(request))
			.isInstanceOf(LuckKidsException.class)
			.hasMessage("해당 사용자가 알림설정이 되어있지 않습니다.");
	}

	@DisplayName("userId로 AlertSetting Entity를 조회한다.")
	@Test
	void findALlByUserId() {
		User user = createUser(1);
		Push push = createPushWishDeviceId(user, "test1");
		createAlertSetting(push);

		Push push2 = createPushWishDeviceId(user, "test2");
		createAlertSetting(push2);

		List<AlertSetting> alertSetting = alertSettingReadService.findAllByUserId(user.getId());

		assertThat(alertSetting).extracting("entire", "mission", "notice", "friend", "luckMessage")
			.containsExactlyInAnyOrder(
				tuple(CHECKED, CHECKED, CHECKED, CHECKED, CHECKED),
				tuple(CHECKED, CHECKED, CHECKED, CHECKED, CHECKED)
			);
	}

	private User createUser(int i) {
		return userRepository.save(
			User.builder()
				.email("test" + i)
				.password("password")
				.snsType(NORMAL)
				.build());
	}

	private Push createPush(User user) {
		return pushRepository.save(Push.builder()
			.deviceId("testDeviceId")
			.pushToken("testPushToken")
			.user(user)
			.build());
	}

	private Push createPushWishDeviceId(User user, String deviceId) {
		return pushRepository.save(Push.builder()
			.deviceId(deviceId)
			.pushToken("testPushToken")
			.user(user)
			.build());
	}

	private void createAlertSetting(Push push) {
		AlertSetting alertSetting = AlertSetting.builder()
			.push(push)
			.entire(CHECKED)
			.mission(CHECKED)
			.notice(CHECKED)
			.friend(CHECKED)
			.luckMessage(CHECKED)
			.build();

		alertSettingRepository.save(alertSetting);
	}

	private LoginUserInfo createLoginUserInfo(int userId) {
		return LoginUserInfo.builder()
			.userId(userId)
			.build();
	}

}
