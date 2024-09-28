package com.luckkids.api.service.user;

import static com.luckkids.domain.luckkidsCharacter.CharacterType.*;
import static com.luckkids.domain.user.Role.*;
import static com.luckkids.domain.user.SettingStatus.*;
import static com.luckkids.domain.user.SnsType.*;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.user.request.UserFindEmailServiceRequest;
import com.luckkids.api.service.user.response.UserFindSnsTypeResponse;
import com.luckkids.api.service.user.response.UserLeagueResponse;
import com.luckkids.api.service.user.response.UserResponse;
import com.luckkids.domain.friend.Friend;
import com.luckkids.domain.friend.FriendRepository;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.domain.userCharacter.UserCharacterRepository;
import com.luckkids.jwt.dto.LoginUserInfo;

class UserReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private UserReadService userReadService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserCharacterRepository userCharacterRepository;

	@Autowired
	private LuckkidsCharacterRepository luckkidsCharacterRepository;

	@Autowired
	private FriendRepository friendRepository;

	@AfterEach
	void tearDown() {
		userCharacterRepository.deleteAllInBatch();
		luckkidsCharacterRepository.deleteAllInBatch();
		friendRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("유저의 id값을 받아서 유저가 있는지 검색한다.")
	@Test
	void findByOne() {
		// given
		User user = createUser("user@daum.net", "user1234!", KAKAO, "테스트", "테스트 행운문구", 0);
		User savedUser = userRepository.save(user);
		int userId = savedUser.getId();

		// when
		User result = userReadService.findByOne(userId);

		// then
		assertThat(result)
			.extracting("email", "password", "snsType")
			.contains("user@daum.net", user.getPassword(), KAKAO);
	}

	@DisplayName("유저의 id값을 받아서 유저가 있는지 검색했을 때 없는 유저의 예외상황이 발생한다.")
	@Test
	void findByOneWithException() {
		// given
		int userId = 0;

		// when // then
		assertThatThrownBy(() -> userReadService.findByOne(userId))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("해당 유저는 없습니다. id = " + userId);

	}

	@DisplayName("유저의 id값을 받아서 유저가 있는지 검색한다.")
	@Test
	void findById() {
		// given
		User user = createUser("user@daum.net", "user1234!", KAKAO, "테스트", "테스트 행운문구", 0);
		User savedUser = userRepository.save(user);
		int userId = savedUser.getId();

		// when
		UserResponse result = userReadService.findById(userId);

		// then
		assertThat(result)
			.extracting("email", "nickname", "snsType", "luckPhrase", "role", "settingStatus", "missionCount")
			.contains("user@daum.net", "테스트", KAKAO, "테스트 행운문구", USER, COMPLETE, 0);
	}

	@DisplayName("이메일을 받아서 해당 이메일을 사용하는 사용자를 가져온다")
	@Test
	void findByEmail() {
		// given
		User user = createUser("test@email.com", "1234", NORMAL, "테스트", "테스트 행운문구", 0);
		userRepository.save(user);
		// when
		User findUser = userReadService.findByEmail(user.getEmail());
		// then
		assertThat(findUser).extracting("email", "password", "snsType")
			.contains("test@email.com", user.getPassword(), NORMAL);
	}

	@DisplayName("이메일을 받아서 해당 이메일을 사용하는 사용자를 조회시 존재하지 않는 사용자일 경우 예외를 발생시킨다.")
	@Test
	void findByEmailThrowException() {
		// given

		// when then
		assertThatThrownBy(() -> userReadService.findByEmail("test@email.com"))
			.isInstanceOf(LuckKidsException.class)
			.hasMessage("해당 이메일을 사용중인 사용자가 존재하지 않습니다.");
	}

	@DisplayName("입력받은 이메일의 회원가입 형태를 확인한다.")
	@Test
	void findSnsTypeTest() {
		User user = createUser("test@email.com", "1234", KAKAO, "테스트", "테스트 행운문구", 0);
		userRepository.save(user);

		UserFindEmailServiceRequest userFindEmailServiceRequest = UserFindEmailServiceRequest.builder()
			.email("test@email.com")
			.build();

		UserFindSnsTypeResponse userFindSnsTypeResponse = userReadService.findEmail(userFindEmailServiceRequest);

		assertThat(userFindSnsTypeResponse.getSnsType()).isEqualTo(KAKAO);
	}

	@DisplayName("입력받은 이메일의 회원가입 형태를 확인시 사용자가 존재하지않으면 예외를 발생시킨다.")
	@Test
	void findSnsTypeTestThrowException() {
		UserFindEmailServiceRequest userFindEmailServiceRequest = UserFindEmailServiceRequest.builder()
			.email("test@email.com")
			.build();

		assertThatThrownBy(() -> userReadService.findEmail(userFindEmailServiceRequest))
			.isInstanceOf(LuckKidsException.class)
			.hasMessage("해당 이메일을 사용중인 사용자가 존재하지 않습니다.");
	}

	@DisplayName("유저들의 1-3위까지 리그 정보를 조회한다.")
	@Test
	void getUserLeagueTop3() {
		// given
		User user1 = createUser("test1@gmail.com", "test1234", NORMAL, "테스트1", "테스트1의 행운문구", 20);
		User user2 = createUser("test2@gmail.com", "test1234", NORMAL, "테스트2", "테스트2의 행운문구", 200);
		User user3 = createUser("test3@gmail.com", "test1234", NORMAL, "테스트3", "테스트3의 행운문구", 110);
		User user4 = createUser("test4@gmail.com", "test1234", NORMAL, "테스트4", "테스트4의 행운문구", 150);
		userRepository.saveAll(List.of(user1, user2, user3, user4));

		Friend friend1 = createFriend(user1, user2);
		Friend friend2 = createFriend(user1, user3);
		Friend friend3 = createFriend(user3, user2);
		friendRepository.saveAll(List.of(friend1, friend2, friend3));

		LuckkidsCharacter luckkidsCharacter1 = createLuckkidsCharacter(CLOVER, 1);
		LuckkidsCharacter luckkidsCharacter2 = createLuckkidsCharacter(CLOVER, 3);
		luckkidsCharacterRepository.saveAll(List.of(luckkidsCharacter1, luckkidsCharacter2));

		UserCharacter userCharacter1 = createUserCharacter(user1, luckkidsCharacter1);
		UserCharacter userCharacter2 = createUserCharacter(user2, luckkidsCharacter1);
		UserCharacter userCharacter3 = createUserCharacter(user3, luckkidsCharacter2);
		UserCharacter userCharacter4 = createUserCharacter(user4, luckkidsCharacter1);
		userCharacterRepository.saveAll(List.of(userCharacter1, userCharacter2, userCharacter3, userCharacter4));

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user1.getId()));

		// when
		List<UserLeagueResponse> userLeagues = userReadService.getUserLeagueTop3();

		// then
		assertThat(userLeagues)
			.extracting("nickname", "characterType", "level", "characterCount")
			.containsExactly(
				tuple("테스트2", CLOVER, 1, 2),
				tuple(null, CLOVER, 1, 1),
				tuple("테스트3", CLOVER, 3, 1)
			);
	}

	@DisplayName("현재 캐릭터 레벨업까지의 달성률을 가져온다.")
	@Test
	void getCharacterAchievementRate() {
		// given
		User user = createUser("test1@gmail.com", "test1234", NORMAL, "테스트1", "테스트1의 행운문구", 20);
		userRepository.save(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		// when
		double characterAchievementRate = userReadService.getCharacterAchievementRate();

		// then
		assertThat(characterAchievementRate).isEqualTo(0.2);
	}

	@DisplayName("현재 로그인한 유저정보를 가져온다.")
	@Test
	void findByMe() {
		// given
		User user = createUser("test1@gmail.com", "test1234", NORMAL, "테스트1", "테스트1의 행운문구", 20);
		userRepository.save(user);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		// when
		UserResponse me = userReadService.findByMe();

		// then
		assertThat(me.getEmail()).isEqualTo(user.getEmail());
	}

	private User createUser(String email, String password, SnsType snsType, String nickname, String luckPhrase,
		int missionCount) {
		return User.builder()
			.email(email)
			.password(password)
			.snsType(snsType)
			.nickname(nickname)
			.luckPhrase(luckPhrase)
			.role(USER)
			.settingStatus(COMPLETE)
			.missionCount(missionCount)
			.build();
	}

	private Friend createFriend(User requester, User receiver) {
		return Friend.builder()
			.requester(requester)
			.receiver(receiver)
			.build();
	}

	private LuckkidsCharacter createLuckkidsCharacter(CharacterType characterType, int level) {
		return LuckkidsCharacter.builder()
			.characterType(characterType)
			.level(level)
			.build();
	}

	private UserCharacter createUserCharacter(User user, LuckkidsCharacter luckkidsCharacter) {
		return UserCharacter.builder()
			.user(user)
			.luckkidsCharacter(luckkidsCharacter)
			.characterProgressStatus(IN_PROGRESS)
			.build();
	}

	private LoginUserInfo createLoginUserInfo(int userId) {
		return LoginUserInfo.builder()
			.userId(userId)
			.build();
	}
}