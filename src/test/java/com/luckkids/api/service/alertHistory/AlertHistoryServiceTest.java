package com.luckkids.api.service.alertHistory;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.alertHistory.AlertHistoryRepository;
import com.luckkids.domain.push.Push;
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

	// ⭐️ 테스트 수정 !
	// @DisplayName("푸시알림 발송 이력을 저장한다.")
	// @Test
	// void save(){
	//     User user = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
	//     userRepository.save(user);
	//
	//     Push push = createPush(user);
	//     pushRepository.save(push);
	//
	//     AlertHistoryServiceRequest alertHistoryServiceRequest = AlertHistoryServiceRequest.builder()
	//             .push(push)
	//             .alertDescription("알림테스트 내용")
	//             .build();
	//
	//     AlertHistory alertHistory = alertHistoryService.createAlertHistory(alertHistoryServiceRequest);
	//     assertThat(alertHistory).extracting("push", "alertDescription", "alertHistoryStatus")
	//             .contains(push, "알림테스트 내용", AlertHistoryStatus.UNCHECKED);
	// }

	private Push createPush(User user) {
		return Push.builder()
			.user(user)
			.pushToken("testToken")
			.deviceId("testDevice")
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
}
