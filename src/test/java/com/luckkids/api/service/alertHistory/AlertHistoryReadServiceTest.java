package com.luckkids.api.service.alertHistory;

import static com.luckkids.domain.alertHistory.AlertHistoryStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import com.luckkids.api.page.request.PageInfoServiceRequest;
import com.luckkids.domain.alertHistory.AlertDestinationType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.page.response.PageCustom;
import com.luckkids.api.service.alertHistory.request.AlertHistoryDeviceIdServiceRequest;
import com.luckkids.api.service.alertHistory.response.AlertHistoryResponse;
import com.luckkids.domain.alertHistory.AlertHistory;
import com.luckkids.domain.alertHistory.AlertHistoryRepository;
import com.luckkids.domain.alertHistory.AlertHistoryStatus;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.jwt.dto.LoginUserInfo;

class AlertHistoryReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private AlertHistoryReadService alertHistoryReadService;

	@Autowired
	private AlertHistoryRepository alertHistoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PushRepository pushRepository;

	@AfterEach
	void tearDown() {
		alertHistoryRepository.deleteAllInBatch();
		pushRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("알림 내역 ID를 받아서 알림 내역을 가져온다.")
	@Test
	void findByOne() {
		// given
		User user = createUser();
		userRepository.save(user);

		Push push = createPush(user);
		pushRepository.save(push);

		AlertHistory alertHistory = createAlertHistory(user, "test1", CHECKED);
		alertHistoryRepository.save(alertHistory);

		// when
		AlertHistory result = alertHistoryReadService.findByOne(alertHistory.getId());

		// then
		assertThat(result).extracting("id", "alertDescription", "alertHistoryStatus")
			.contains(alertHistory.getId(), alertHistory.getAlertDescription(), alertHistory.getAlertHistoryStatus());
	}

	@DisplayName("알림 내역 ID를 받아서 알림 내역이 있는지 검색했을 때 ID가 없는 예외상황이 발생한다.")
	@Test
	void findByOneWithException() {
		// given
		Long id = 0L;

		// when // then
		assertThatThrownBy(() -> alertHistoryReadService.findByOne(id))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("해당 알림 내역은 없습니다. id = " + id);
	}

	@DisplayName("알림 내역들을 가져온다.")
	@Test
	void getAlertHistory() {
		// given
		User user = createUser();
		userRepository.save(user);

		Push push = createPush(user);
		pushRepository.save(push);

		AlertHistory alertHistory = createAlertHistory(user, "test1", CHECKED);
		AlertHistory savedAlertHistory = alertHistoryRepository.save(alertHistory);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		PageInfoServiceRequest request = PageInfoServiceRequest.builder()
			.page(1)
			.size(10)
			.build();

		AlertHistoryResponse response = AlertHistoryResponse.of(savedAlertHistory);

		// when
		PageCustom<AlertHistoryResponse> result = alertHistoryReadService.getAlertHistory(request);

		// then
		assertThat(result.getContent()).extracting("id", "alertDescription", "alertHistoryStatus")
			.contains(
				tuple(response.getId(), response.getAlertDescription(), response.getAlertHistoryStatus())
			);

		assertThat(result.getPageInfo()).extracting("currentPage", "totalPage", "totalElement")
			.contains(1, 1, 1);
	}

	@DisplayName("유저가 읽지 않은 알림이 하나 이상인지 확인한다.")
	@Test
	void hasUncheckedAlerts() {
		// given
		User user1 = createUser();
		User user2 = createUser();
		userRepository.saveAll(List.of(user1, user2));

		Push push1 = createPush(user1);
		Push push2 = createPush(user2);
		pushRepository.saveAll(List.of(push1, push2));

		AlertHistory alertHistory1 = createAlertHistory(user1, "test1", CHECKED);
		AlertHistory alertHistory2 = createAlertHistory(user1, "test2", CHECKED);
		AlertHistory alertHistory3 = createAlertHistory(user1, "test3", CHECKED);
		AlertHistory alertHistory4 = createAlertHistory(user2, "test4", UNCHECKED);
		alertHistoryRepository.saveAll(List.of(alertHistory1, alertHistory2, alertHistory3, alertHistory4));

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user1.getId()));

		// when
		boolean result = alertHistoryReadService.hasUncheckedAlerts();

		// then
		assertThat(result).isFalse();
	}

	@DisplayName("유저가 이미 요청받은 초대인지 확인한다.")
	@Test
	void hasFriendCodeAlert() {
		// given
		User user1 = createUser();
		userRepository.save(user1);

		Push push1 = createPush(user1);
		pushRepository.save(push1);

		AlertHistory alertHistory1 = createFriendCodeAlertHistory(user1, "test", CHECKED);
		alertHistoryRepository.save(alertHistory1);

		// when
		boolean result = alertHistoryReadService.hasFriendCode(user1.getId(), "test");

		// then
		assertThat(result).isTrue();
	}

	private User createUser() {
		return User.builder()
			.email("test@email.com")
			.password("1234")
			.snsType(SnsType.NORMAL)
			.build();
	}

	private Push createPush(User user) {
		return Push.builder()
			.user(user)
			.deviceId("testdeviceId")
			.pushToken("testPushToken")
			.build();
	}

	private AlertHistory createAlertHistory(User user, String alertDescription, AlertHistoryStatus alertHistoryStatus) {
		return AlertHistory.builder()
			.user(user)
			.alertDescription(alertDescription)
			.alertHistoryStatus(alertHistoryStatus)
			.build();
	}

	private AlertHistory createFriendCodeAlertHistory(User user, String alertDescriptionInfo,AlertHistoryStatus alertHistoryStatus) {
		return AlertHistory.builder()
				.user(user)
				.alertDestinationType(AlertDestinationType.FRIEND_CODE)
				.alertDestinationInfo(alertDescriptionInfo)
				.alertHistoryStatus(alertHistoryStatus)
				.build();
	}

	private LoginUserInfo createLoginUserInfo(int userId) {
		return LoginUserInfo.builder()
			.userId(userId)
			.build();
	}
}
