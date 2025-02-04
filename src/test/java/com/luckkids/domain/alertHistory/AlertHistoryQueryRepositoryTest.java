package com.luckkids.domain.alertHistory;

import static com.luckkids.notification.domain.alertHistory.AlertHistoryStatus.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.page.request.PageInfoServiceRequest;
import com.luckkids.notification.domain.alertHistory.AlertHistory;
import com.luckkids.notification.domain.alertHistory.AlertHistoryStatus;
import com.luckkids.notification.domain.push.Push;
import com.luckkids.notification.infra.AlertHistoryQueryRepository;
import com.luckkids.notification.infra.AlertHistoryRepository;
import com.luckkids.notification.infra.PushRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;

@Transactional
class AlertHistoryQueryRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private AlertHistoryQueryRepository alertHistoryQueryRepository;

	@Autowired
	private AlertHistoryRepository alertHistoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PushRepository pushRepository;

	@DisplayName("페이징 정보를 받아서 페이징 처리된 알림 내역을 조회한다.")
	@Test
	void findByUserId() {
		// given
		User user = createUser();
		userRepository.save(user);

		Push push = createPush(user);
		pushRepository.save(push);

		AlertHistory alertHistory = createAlertHistory(user, "test1", CHECKED);
		AlertHistory savedAlertHistory = alertHistoryRepository.save(alertHistory);

		Pageable pageable = PageInfoServiceRequest.builder()
			.page(1)
			.size(10)
			.build()
			.toPageable();

		// when
		Page<AlertHistory> result = alertHistoryQueryRepository.findByUserId(user.getId(), pageable);

		// then
		assertThat(result.getContent())
			.extracting("alertDescription", "alertHistoryStatus")
			.contains(
				tuple(
					savedAlertHistory.getAlertDescription(),
					savedAlertHistory.getAlertHistoryStatus()
				)
			);

		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getTotalPages()).isEqualTo(1);
	}

	@DisplayName("유저가 읽지 않은 알림이 하나 이상인지 확인한다. (읽지 않은 알림이 있음)")
	@Test
	void hasUncheckedAlerts_1() {
		// given
		User user1 = createUser();
		User user2 = createUser();
		userRepository.saveAll(List.of(user1, user2));

		Push push1 = createPush(user1);
		Push push2 = createPush(user2);
		pushRepository.saveAll(List.of(push1, push2));

		AlertHistory alertHistory1 = createAlertHistory(user1, "test1", CHECKED);
		AlertHistory alertHistory2 = createAlertHistory(user1, "test2", UNCHECKED);
		AlertHistory alertHistory3 = createAlertHistory(user1, "test3", CHECKED);
		AlertHistory alertHistory4 = createAlertHistory(user2, "test4", CHECKED);
		alertHistoryRepository.saveAll(List.of(alertHistory1, alertHistory2, alertHistory3, alertHistory4));

		// when
		boolean result = alertHistoryQueryRepository.hasUncheckedAlerts(user1.getId());

		// then
		assertThat(result).isTrue();
	}

	@DisplayName("유저가 읽지 않은 알림이 하나 이상인지 확인한다. (읽지 않은 알림이 없음)")
	@Test
	void hasUncheckedAlerts_2() {
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

		// when
		boolean result = alertHistoryQueryRepository.hasUncheckedAlerts(user1.getId());

		// then
		assertThat(result).isFalse();
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
}