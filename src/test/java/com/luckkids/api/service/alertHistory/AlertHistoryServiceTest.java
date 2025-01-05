package com.luckkids.api.service.alertHistory;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.alertHistory.request.AlertHistoryServiceRequest;
import com.luckkids.domain.alertHistory.AlertDestinationType;
import com.luckkids.domain.alertHistory.AlertHistory;
import com.luckkids.domain.alertHistory.AlertHistoryRepository;
import com.luckkids.domain.alertHistory.AlertHistoryStatus;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SettingStatus;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;

public class AlertHistoryServiceTest extends IntegrationTestSupport {

	@Autowired
	private AlertHistoryService alertHistoryService;

	@Autowired
	private AlertHistoryRepository alertHistoryRepository;

	@Autowired
	private PushRepository pushRepository;

	@Autowired
	private UserRepository userRepository;

	@AfterEach
	void tearDown() {
		alertHistoryRepository.deleteAllInBatch();
		pushRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("푸시알림 발송 이력을 저장한다.")
	@Test
	void save() {
		User user = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
		userRepository.save(user);

		AlertHistoryServiceRequest alertHistoryServiceRequest = AlertHistoryServiceRequest.builder()
			.user(user)
			.alertDescription("알림테스트 내용")
			.alertDestinationType(AlertDestinationType.FRIEND_CODE)
			.alertDestinationInfo("테스트")
			.build();

		AlertHistory alertHistory = alertHistoryService.createUnCheckedAlertHistory(alertHistoryServiceRequest);
		assertThat(alertHistory).extracting("user", "alertDescription", "alertHistoryStatus", "alertDestinationType",
				"alertDestinationInfo")
			.contains(user, "알림테스트 내용", AlertHistoryStatus.UNCHECKED, AlertDestinationType.FRIEND_CODE, "테스트");
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
}
