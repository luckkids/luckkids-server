package com.luckkids.notification.service.push;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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
import com.luckkids.jwt.dto.LoginUserInfo;
import com.luckkids.notification.domain.push.Push;
import com.luckkids.notification.domain.push.PushMessage;
import com.luckkids.notification.infra.PushRepository;
import com.luckkids.notification.service.PushService;
import com.luckkids.notification.service.request.PushSoundChangeServiceRequest;
import com.luckkids.notification.service.response.PushSoundChangeResponse;

public class PushServiceTest extends IntegrationTestSupport {

	@Autowired
	private PushService pushService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PushRepository pushRepository;

	@AfterEach
	void tearDown() {
		pushRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("푸시 사운드를 무음으로 수정한다.")
	@Test
	void updateSoundTest() {
		User user = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
		userRepository.save(user);

		Push push = createPush(user);
		pushRepository.save(push);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		PushSoundChangeServiceRequest pushSoundChangeServiceRequest = PushSoundChangeServiceRequest.builder()
			.deviceId("테스트")
			.build();

		PushSoundChangeResponse pushSoundChangeResponse = pushService.updateSound(pushSoundChangeServiceRequest);

		assertThat(pushSoundChangeResponse).extracting("deviceId", "pushToken", "sound")
			.contains("테스트", "testToken", null);
	}

	@DisplayName("푸시 사운드를 다른 사운드명으로 수정한다.")
	@Test
	void updateSoundWithSoundNameTest() {
		User user = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
		userRepository.save(user);

		Push push = createPush(user);
		pushRepository.save(push);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		PushSoundChangeServiceRequest pushSoundChangeServiceRequest = PushSoundChangeServiceRequest.builder()
			.deviceId("테스트")
			.sound("sound.wav")
			.build();

		PushSoundChangeResponse pushSoundChangeResponse = pushService.updateSound(pushSoundChangeServiceRequest);

		assertThat(pushSoundChangeResponse).extracting("deviceId", "pushToken", "sound")
			.contains("테스트", "testToken", "sound.wav");
	}

	private Push createPush(User user) {
		return Push.builder()
			.user(user)
			.pushToken("testToken")
			.deviceId("테스트")
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

	private LoginUserInfo createLoginUserInfo(int userId) {
		return LoginUserInfo.builder()
			.userId(userId)
			.build();
	}

}
