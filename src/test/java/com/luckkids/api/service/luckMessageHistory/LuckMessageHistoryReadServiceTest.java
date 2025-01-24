package com.luckkids.api.service.luckMessageHistory;

import static com.luckkids.domain.user.SnsType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.domain.luckMessageHistory.LuckMessageHistory;
import com.luckkids.domain.luckMessageHistory.LuckMessageHistoryRepository;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.jwt.dto.LoginUserInfo;

public class LuckMessageHistoryReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private LuckMessageHistoryReadService luckMessageHistoryReadService;
	@Autowired
	private LuckMessageHistoryRepository luckMessageHistoryRepository;
	@Autowired
	private PushRepository pushRepository;
	@Autowired
	private UserRepository userRepository;

	@AfterEach
	public void tearDown() {
		luckMessageHistoryRepository.deleteAllInBatch();
		pushRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("오늘의 행운의 한마디를 조회한다.")
	@Test
	void findByDeviceIdAndUserIdTest() {
		User savedUser = createUser(1);
		Push savedPush = createPush(savedUser);
		LuckMessageHistory savedLuckMessageHistory = createLuckMessageHistory(savedPush);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(savedUser.getId()));

		luckMessageHistoryRepository.save(savedLuckMessageHistory);
		LuckMessageHistory findHistory = luckMessageHistoryReadService.findByDeviceIdAndUserId("testDeviceId");

		assertThat(findHistory.getMessageDescription()).isEqualTo("테스트 한마디");
	}

	@DisplayName("오늘의 행운의 한마디를 조회시 존재하지 않으면 예외가 발생한다.")
	@Test
	void findByDeviceIdAndUserIdThrowExceptionTest() {
		User savedUser = createUser(1);
		Push savedPush = createPush(savedUser);
		createLuckMessageHistory(savedPush);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(savedUser.getId()));

		assertThatThrownBy(() -> luckMessageHistoryReadService.findByDeviceIdAndUserId("testDeviceId"))
			.isInstanceOf(LuckKidsException.class)
			.hasMessage("오늘의 한마디 이력이 없습니다.");
	}

	private LuckMessageHistory createLuckMessageHistory(Push push) {
		return LuckMessageHistory.builder()
			.messageDescription("테스트 한마디")
			.push(push)
			.build();
	}

	private Push createPush(User user) {
		return pushRepository.save(Push.builder()
			.deviceId("testDeviceId")
			.pushToken("testPushToken")
			.user(user)
			.build());
	}

	private LoginUserInfo createLoginUserInfo(int userId) {
		return LoginUserInfo.builder()
			.userId(userId)
			.build();
	}

	private User createUser(int i) {
		return userRepository.save(
			User.builder()
				.email("test" + i)
				.password("password")
				.snsType(NORMAL)
				.build());
	}
}
