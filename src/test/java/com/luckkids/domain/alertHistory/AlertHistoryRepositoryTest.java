package com.luckkids.domain.alertHistory;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;

@Transactional
public class AlertHistoryRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private AlertHistoryRepository alertHistoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PushRepository pushRepository;

	@Test
	@DisplayName("사용자의 알림목록을 삭제한다.")
	void deleteByPushUserId() {
		// given
		User user = createUser();
		userRepository.save(user);

		Push push = createPush(user);
		pushRepository.save(push);

		AlertHistory alertHistory = createAlertHistory(user);
		AlertHistory savedAlertHistory = alertHistoryRepository.save(alertHistory);

		// when
		alertHistoryRepository.deleteByUserId(user.getId());

		// then
		Optional<AlertHistory> findAlertHistory = alertHistoryRepository.findById(savedAlertHistory.getId());
		assertThat(findAlertHistory.isEmpty()).isTrue();
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
