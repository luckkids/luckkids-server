package com.luckkids.domain.user;

import static com.luckkids.domain.user.SettingStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.refreshToken.RefreshToken;
import com.luckkids.domain.refreshToken.RefreshTokenRepository;
import com.luckkids.jwt.dto.JwtToken;

@Transactional
public class UserTest extends IntegrationTestSupport {

	@Autowired
	private PushRepository pushRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private UserReadService userReadService;

	@DisplayName("같은 deviceid로 저장되어있는 pushkey와 상이한 데이터가 들어올시 수정한다.")
	@Test
	void checkPushAndUpdate() {
		// given
		User user = User.builder()
			.email("tkdrl8908@naver.com")
			.password("1234")
			.role(Role.USER)
			.snsType(SnsType.NORMAL)
			.settingStatus(SettingStatus.INCOMPLETE)
			.build();

		User savedUser = userRepository.save(user);

		Push push = Push.builder()
			.pushToken("testPushKey")
			.user(savedUser)
			.deviceId("testDeviceId")
			.build();

		Push savedPush = pushRepository.save(push);

		// when
		savedUser.checkPushKey("testPushKey2", "testDeviceId");

		assertThat(savedUser.getPushes().get(0))
			.extracting("pushToken", "deviceId")
			.containsExactlyInAnyOrder(
				"testPushKey2", "testDeviceId"
			);
	}

	@DisplayName("같은 deviceid로 저장되어있는 pushkey가 없을 시 저장한다.")
	@Test
	void checkPushAndSave() {
		// given
		User user = User.builder()
			.email("tkdrl8908@naver.com")
			.password("1234")
			.role(Role.USER)
			.snsType(SnsType.NORMAL)
			.settingStatus(SettingStatus.INCOMPLETE)
			.build();

		User savedUser = userRepository.save(user);

		// when
		savedUser.checkPushKey("testPushKey", "testDeviceId");

		// then
		List<Push> pushes = savedUser.getPushes();

		assertThat(pushes).hasSize(1)
			.extracting("pushToken", "deviceId")
			.containsExactlyInAnyOrder(
				tuple("testPushKey", "testDeviceId")
			);
	}

	@DisplayName("같은 deviceid로 저장되어있는 refreshToken과 상이한 데이터가 들어올시 저장한다.")
	@Test
	void checkRefreshTokenAndUpdate() {
		// given
		User user = User.builder()
			.email("tkdrl8908@naver.com")
			.password("1234")
			.role(Role.USER)
			.snsType(SnsType.NORMAL)
			.settingStatus(SettingStatus.INCOMPLETE)
			.build();

		User savedUser = userRepository.save(user);

		RefreshToken refreshToken = RefreshToken.builder()
			.refreshToken("testRefreshToken1")
			.deviceId("testDeviceId")
			.build();

		refreshToken.setUser(savedUser);

		RefreshToken savedToken = refreshTokenRepository.save(refreshToken);

		JwtToken jwtToken = JwtToken.builder()
			.accessToken("accesstoken")
			.refreshToken("testRefreshToken2")
			.grantType("Bearer")
			.expiresIn(1L)
			.build();

		// when
		savedUser.checkRefreshToken(jwtToken, "testDeviceId");

		// then
		List<RefreshToken> getToken = savedUser.getRefreshTokens();

		assertThat(getToken)
			.extracting("refreshToken", "deviceId")
			.containsExactlyInAnyOrder(
				tuple("testRefreshToken2", "testDeviceId")
			);
	}

	@DisplayName("같은 deviceid로 저장되어있는 refreshToken가 없을 시 수정한다.")
	@Test
	void checkRefreshTokenAndSave() {
		// given
		User user = User.builder()
			.email("tkdrl8908@naver.com")
			.password("1234")
			.role(Role.USER)
			.snsType(SnsType.NORMAL)
			.settingStatus(SettingStatus.INCOMPLETE)
			.build();

		User savedUser = userRepository.save(user);

		JwtToken jwtToken = JwtToken.builder()
			.accessToken("accesstoken")
			.refreshToken("testRefreshToken")
			.grantType("Bearer")
			.expiresIn(1L)
			.build();

		// when
		savedUser.checkRefreshToken(jwtToken, "testDeviceId");

		// then
		List<RefreshToken> getToken = savedUser.getRefreshTokens();

		assertThat(getToken).hasSize(1)
			.extracting("refreshToken", "deviceId")
			.containsExactlyInAnyOrder(
				tuple("testRefreshToken", "testDeviceId")
			);
	}

	@DisplayName("사용자 비밀번호를 변경한다.")
	@Test
	@Transactional
	void updatePasswordTest() {
		User user = createUser("test@email.com", "1234", SnsType.NORMAL, 0);
		String beforePassword = user.getPassword();
		User savedUser = userRepository.save(user);
		savedUser.updatePassword("123456");
		String afterPassword = savedUser.getPassword();

		assertThat(beforePassword).isNotEqualTo(afterPassword);
	}

	@DisplayName("사용자의 행운문구를 수정한다.")
	@Test
	void updateLuckPhraseTest() {
		// given
		User user = User.builder()
			.email("tkdrl8908@naver.com")
			.password("1234")
			.role(Role.USER)
			.snsType(SnsType.NORMAL)
			.settingStatus(SettingStatus.INCOMPLETE)
			.build();

		User savedUser = userRepository.save(user);

		// when
		User findUser = userReadService.findByOne(savedUser.getId());
		findUser.updateSettingStatus(COMPLETE);

		// then
		assertThat(findUser.getSettingStatus()).isEqualTo(COMPLETE);
		savedUser.updateLuckPhrase("행운입니다!!");
		savedUser.updateSettingStatus(INCOMPLETE);

		// then
		assertThat(savedUser)
			.extracting("email", "luckPhrase", "role", "snsType", "settingStatus")
			.contains(
				"tkdrl8908@naver.com", "행운입니다!!", Role.USER, SnsType.NORMAL, SettingStatus.INCOMPLETE
			);
	}

	@DisplayName("유효한 missionCount 값으로 레벨업 카운트를 계산한다.")
	@Test
	void calculateLevelUpCountWithCharacterCount0() {
		// given
		User user = createUser("test@email.com", "1234", SnsType.NORMAL, 20);

		// when
		int count = user.calculateRemainingMissions();

		// then
		assertThat(count).isEqualTo(20);
	}

	@DisplayName("유효한 missionCount 값으로 레벨업 카운트를 계산한다.")
	@Test
	void calculateLevelUpCountWithCharacterCount1() {
		// given
		User user = createUser("test@email.com", "1234", SnsType.NORMAL, 100);

		// when
		int count = user.calculateRemainingMissions();

		// then
		assertThat(count).isEqualTo(100);
	}

	@DisplayName("유효한 missionCount 값으로 레벨업 카운트를 계산한다.")
	@Test
	void calculateLevelUpCountWithCharacterCount2() {
		// given
		User user = createUser("test@email.com", "1234", SnsType.NORMAL, 101);

		// when
		int count = user.calculateRemainingMissions();

		// then
		assertThat(count).isEqualTo(1);
	}

	@DisplayName("유효한 missionCount 값으로 레벨업 카운트를 계산하여 레벨을 가져온다. 레벨있음.")
	@Test
	void calculateLevelBasedOnRemainingMissionsLevelUp_O() {
		User user = createUser("test@email.com", "1234", SnsType.NORMAL, 25);

		// when
		int level = user.calculateLevelBasedOnRemainingMissions();

		// then
		assertThat(level).isEqualTo(2);
	}

	@DisplayName("유효한 missionCount 값으로 레벨업 카운트를 계산하여 레벨을 가져온다. 레벨없음.")
	@Test
	void calculateLevelBasedOnRemainingMissionsLevelUp_X() {
		User user = createUser("test@email.com", "1234", SnsType.NORMAL, 15);

		// when
		int level = user.calculateLevelBasedOnRemainingMissions();

		// then
		assertThat(level).isEqualTo(0);
	}

	@DisplayName("유저 닉네임을 수정한다.")
	@Test
	void updateNickName() {
		User user = createUser("test@email.com", "1234", SnsType.NORMAL, 15);
		userRepository.save(user);
		// when
		user.updateNickName("럭키즈!!!!!");

		User findUser = userReadService.findByOne(user.getId());

		// then
		assertThat(findUser.getNickname()).isEqualTo("럭키즈!!!!!");
	}

	@DisplayName("미션 카운트를 이용해서 레벨업까지의 달성률을 계산한다.")
	@Test
	void calculateAchievementRate() {
		// given
		User user1 = createUser("test1@email.com", "1234", SnsType.NORMAL, 15);
		User user2 = createUser("test2@email.com", "1234", SnsType.NORMAL, 155);
		User user3 = createUser("test3@email.com", "1234", SnsType.NORMAL, 25);

		// when
		double rate1 = user1.calculateAchievementRate();
		double rate2 = user2.calculateAchievementRate();
		double rate3 = user3.calculateAchievementRate();

		// then
		assertAll(
			() -> assertThat(rate1).isEqualTo(0.15),
			() -> assertThat(rate2).isEqualTo(0.55),
			() -> assertThat(rate3).isEqualTo(0.55)
		);
	}

	@DisplayName("마지막 로그인 날짜를 저장한다.")
	@Test
	void updateLastLoginDateTest() {
		User user = createUser("test1@email.com", "1234", SnsType.NORMAL, 15);
		user.updateLastLoginDate();

		assertThat(user.getLastLoginDate()).isNotNull();
	}

	private User createUser(String email, String password, SnsType snsType, int missionCount) {
		return User.builder()
			.email(email)
			.password(password)
			.snsType(snsType)
			.missionCount(missionCount)
			.build();
	}
}
