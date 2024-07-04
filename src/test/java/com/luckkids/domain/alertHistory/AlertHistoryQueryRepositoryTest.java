package com.luckkids.domain.alertHistory;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.page.request.PageInfoServiceRequest;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
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

	@DisplayName("페이징 정보와 deviceId를 받아서 페이징 처리된 알림 내역을 조회한다.")
	@Test
	void findByDeviceId() {
		// given
		User user = createUser();
		userRepository.save(user);

		Push push = createPush(user);
		pushRepository.save(push);

		AlertHistory alertHistory = createAlertHistory(user);
		AlertHistory savedAlertHistory = alertHistoryRepository.save(alertHistory);

		Pageable pageable = PageInfoServiceRequest.builder()
			.page(1)
			.size(10)
			.build()
			.toPageable();

		// when
		Page<AlertHistory> result = alertHistoryQueryRepository.findByDeviceIdAndUserId(user.getId(), pageable);

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

	private AlertHistory createAlertHistory(User user) {
		return AlertHistory.builder()
			.user(user)
			.alertDescription("test")
			.alertHistoryStatus(AlertHistoryStatus.CHECKED)
			.build();
	}
}