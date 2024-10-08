package com.luckkids.domain.user;

import static com.luckkids.domain.luckkidsCharacter.CharacterType.*;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;
import com.luckkids.domain.user.projection.InProgressCharacterDto;
import com.luckkids.domain.user.projection.MyProfileDto;
import com.luckkids.domain.user.projection.UserLeagueDto;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.domain.userCharacter.UserCharacterRepository;

@Transactional
class UserQueryRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private UserQueryRepository userQueryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserCharacterRepository userCharacterRepository;

	@Autowired
	private LuckkidsCharacterRepository luckkidsCharacterRepository;

	@DisplayName("나의 프로필을 조회한다.")
	@Test
	void getMyProfile() {
		// given
		User user = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
		userRepository.save(user);

		LuckkidsCharacter luckkidsCharacter = createLuckkidsCharacter(CLOVER, 1);
		luckkidsCharacterRepository.save(luckkidsCharacter);

		UserCharacter userCharacter = createUserCharacter(user, luckkidsCharacter);
		userCharacterRepository.save(userCharacter);

		// when
		MyProfileDto myProfile = userQueryRepository.getMyProfile(user.getId());

		// then
		assertThat(myProfile)
			.extracting("myId", "nickname", "luckPhrase", "characterType", "level", "characterCount")
			.contains(user.getId(), "테스트1", "테스트1의 행운문구", CLOVER, 1, 0);
	}

	@DisplayName("현재 진행중인 캐릭터 정보를 조회한다.")
	@Test
	void getInProgressCharacter() {
		// given
		User user = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
		userRepository.save(user);

		LuckkidsCharacter luckkidsCharacter = createLuckkidsCharacter(CLOVER, 1);
		luckkidsCharacterRepository.save(luckkidsCharacter);

		UserCharacter userCharacter = createUserCharacter(user, luckkidsCharacter);
		userCharacterRepository.save(userCharacter);

		// when
		InProgressCharacterDto inProgressCharacter = userQueryRepository.getInProgressCharacter(user.getId());

		// then
		assertThat(inProgressCharacter)
			.extracting("characterType", "level")
			.contains(CLOVER, 1);
	}

	@DisplayName("유저들의 1-3위까지 리그 정보를 조회한다.")
	@Test
	void getUserLeagueTop3() {
		// given
		User user1 = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 20);
		User user2 = createUser("test2@gmail.com", "test1234", "테스트2", "테스트2의 행운문구", 200);
		User user3 = createUser("test3@gmail.com", "test1234", "테스트3", "테스트3의 행운문구", 110);
		User user4 = createUser("test4@gmail.com", "test1234", "테스트4", "테스트4의 행운문구", 150);
		userRepository.saveAll(List.of(user1, user2, user3, user4));

		LuckkidsCharacter luckkidsCharacter1 = createLuckkidsCharacter(CLOVER, 1);
		LuckkidsCharacter luckkidsCharacter2 = createLuckkidsCharacter(CLOVER, 3);
		luckkidsCharacterRepository.saveAll(List.of(luckkidsCharacter1, luckkidsCharacter2));

		UserCharacter userCharacter1 = createUserCharacter(user1, luckkidsCharacter1);
		UserCharacter userCharacter2 = createUserCharacter(user2, luckkidsCharacter1);
		UserCharacter userCharacter3 = createUserCharacter(user3, luckkidsCharacter2);
		UserCharacter userCharacter4 = createUserCharacter(user4, luckkidsCharacter1);
		userCharacterRepository.saveAll(List.of(userCharacter1, userCharacter2, userCharacter3, userCharacter4));

		// when
		List<UserLeagueDto> userLeagues = userQueryRepository.getUserLeagueTop3();

		// then
		assertThat(userLeagues)
			.extracting("nickname", "characterType", "level", "characterCount")
			.containsExactly(
				tuple("테스트2", CLOVER, 1, 2),
				tuple("테스트4", CLOVER, 1, 1),
				tuple("테스트3", CLOVER, 3, 1)
			);
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
}