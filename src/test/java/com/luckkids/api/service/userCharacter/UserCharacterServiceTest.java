package com.luckkids.api.service.userCharacter;

import static com.luckkids.domain.luckkidsCharacter.CharacterType.*;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.luckkidsCharacter.LuckkidsCharacterReadService;
import com.luckkids.api.service.userCharacter.request.UserCharacterCreateServiceRequest;
import com.luckkids.api.service.userCharacter.response.UserCharacterCreateResponse;
import com.luckkids.api.service.userCharacter.response.UserCharacterLevelUpResponse;
import com.luckkids.api.service.userCharacter.response.UserCharacterSummaryResponse;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.domain.userCharacter.CharacterProgressStatus;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.domain.userCharacter.UserCharacterRepository;
import com.luckkids.jwt.dto.LoginUserInfo;

public class UserCharacterServiceTest extends IntegrationTestSupport {

	@Autowired
	private UserCharacterService userCharacterService;

	@Autowired
	private UserCharacterRepository userCharacterRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LuckkidsCharacterRepository luckkidsCharacterRepository;

	@Autowired
	private LuckkidsCharacterReadService luckkidsCharacterReadService;

	@AfterEach
	void tearDown() {
		userCharacterRepository.deleteAllInBatch();
		luckkidsCharacterRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("사용자가 선택한 캐릭터를 저장한다.")
	@Test
	@Transactional
	void createUserCharacter() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 0);
		userRepository.save(user);

		LuckkidsCharacter luckkidsCharacter = createCharacter(CharacterType.SUN);
		luckkidsCharacterRepository.save(luckkidsCharacter);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		UserCharacterCreateServiceRequest userCharacterCreateServiceRequest = UserCharacterCreateServiceRequest.builder()
			.id(luckkidsCharacter.getId())
			.nickName("럭키즈!")
			.build();

		UserCharacterCreateResponse userCharacterCreateResponse = userCharacterService.createUserCharacter(
			userCharacterCreateServiceRequest);

		// then
		assertThat(userCharacterCreateResponse.getNickName()).isEqualTo("럭키즈!");

		LuckkidsCharacter findLuckkidsCharacter = luckkidsCharacterReadService.findById(luckkidsCharacter.getId());
		UserCharacter userCharacter = userCharacterRepository.findById(userCharacterCreateResponse.getId()).get();

		assertThat(userCharacter.getLuckkidsCharacter()).extracting("id", "characterType", "level", "lottieFile",
				"imageFile")
			.contains(findLuckkidsCharacter.getId(), findLuckkidsCharacter.getCharacterType(),
				findLuckkidsCharacter.getLevel(), findLuckkidsCharacter.getLottieFile(),
				findLuckkidsCharacter.getImageFile());
	}

	@DisplayName("레벨업여부를 결정한다. 레벨업 O")
	@Test
	void determineLevelUpTrue() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 25);
		LuckkidsCharacter luckkidsCharacter1 = createLuckkidsCharacter(CLOVER, 1,
			"https://test.cloudfront.net/test1.json", "https://test.cloudfront.net/test1.png");
		LuckkidsCharacter luckkidsCharacter2 = createLuckkidsCharacter(CLOVER, 2,
			"https://test.cloudfront.net/test2.json", "https://test.cloudfront.net/test2.png");
		UserCharacter userCharacter = createUserCharacter(user, luckkidsCharacter1, IN_PROGRESS);

		userRepository.save(user);
		luckkidsCharacterRepository.saveAll(List.of(luckkidsCharacter1, luckkidsCharacter2));
		userCharacterRepository.save(userCharacter);

		// when
		UserCharacterLevelUpResponse response = userCharacterService.determineLevelUp(user);

		// then
		assertThat(response)
			.extracting("levelUpResult", "lottieFile", "imageFile")
			.contains(true, "https://test.cloudfront.net/test2.json", "https://test.cloudfront.net/test2.png");
	}

	@DisplayName("레벨업여부를 결정한다. 레벨업 X")
	@Test
	void determineLevelUpFalse() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 10);
		LuckkidsCharacter luckkidsCharacter1 = createLuckkidsCharacter(CLOVER, 1,
			"https://test.cloudfront.net/test1.json", "https://test.cloudfront.net/test1.png");
		LuckkidsCharacter luckkidsCharacter2 = createLuckkidsCharacter(CLOVER, 2,
			"https://test.cloudfront.net/test2.json", "https://test.cloudfront.net/test2.png");
		UserCharacter userCharacter = createUserCharacter(user, luckkidsCharacter1, IN_PROGRESS);

		userRepository.save(user);
		luckkidsCharacterRepository.saveAll(List.of(luckkidsCharacter1, luckkidsCharacter2));
		userCharacterRepository.save(userCharacter);

		// when
		UserCharacterLevelUpResponse response = userCharacterService.determineLevelUp(user);

		// then
		assertThat(response)
			.extracting("levelUpResult", "lottieFile", "imageFile")
			.contains(false, null, null);
	}

	@DisplayName("레벨업여부를 결정한다. 레벨업 max")
	@Test
	@Transactional
	void determineLevelUpMax() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 100);
		LuckkidsCharacter luckkidsCharacter = createLuckkidsCharacter(CLOVER, 1,
			"https://test.cloudfront.net/test1.json", "https://test.cloudfront.net/test1.png");
		LuckkidsCharacter luckkidsCharacter1 = createLuckkidsCharacter(CLOVER, 4,
			"https://test.cloudfront.net/test4.json", "https://test.cloudfront.net/test4.png");
		LuckkidsCharacter luckkidsCharacter2 = createLuckkidsCharacter(CLOVER, 5,
			"https://test.cloudfront.net/test5.json", "https://test.cloudfront.net/test5.png");
		UserCharacter userCharacter = createUserCharacter(user, luckkidsCharacter1, IN_PROGRESS);

		userRepository.save(user);
		luckkidsCharacterRepository.saveAll(List.of(luckkidsCharacter, luckkidsCharacter1, luckkidsCharacter2));
		userCharacterRepository.save(userCharacter);

		// when
		UserCharacterLevelUpResponse response = userCharacterService.determineLevelUp(user);

		// then
		assertThat(response)
			.extracting("levelUpResult", "lottieFile", "imageFile")
			.contains(true, "https://test.cloudfront.net/test5.json", "https://test.cloudfront.net/test5.png");

		UserCharacter findUserCharacter = userCharacterRepository.getReferenceById(userCharacter.getId());
		assertThat(findUserCharacter.getCharacterProgressStatus()).isEqualTo(COMPLETED);

		List<UserCharacter> userCharacters = userCharacterRepository.findAll();
		assertThat(userCharacters.size()).isEqualTo(2);

		List<UserCharacter> inProgressUserCharacters = userCharacters.stream()
			.filter(u -> u.getCharacterProgressStatus() == IN_PROGRESS)
			.toList();
		assertThat(inProgressUserCharacters).hasSize(1);
		assertThat(inProgressUserCharacters.get(0).getLuckkidsCharacter().getLevel()).isEqualTo(1);
	}

	@DisplayName("진행중인 캐릭터정보들과 완료한 캐릭터 개수들을 가져온다. (1)")
	@Test
	void getCharacterSummary_1() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 25);
		LuckkidsCharacter luckkidsCharacter = createLuckkidsCharacter(CLOVER, 1,
			"https://test.cloudfront.net/test1.json", "https://test.cloudfront.net/test1.png");
		UserCharacter userCharacter = createUserCharacter(user, luckkidsCharacter, IN_PROGRESS);

		userRepository.save(user);
		luckkidsCharacterRepository.save(luckkidsCharacter);
		userCharacterRepository.save(userCharacter);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		Map<CharacterType, Long> characterCountMap = new EnumMap<>(CharacterType.class);
		for (CharacterType type : CharacterType.values()) {
			characterCountMap.put(type, 0L);
		}

		// when
		UserCharacterSummaryResponse response = userCharacterService.getCharacterSummary();

		// then
		assertThat(response.getInProgressCharacter())
			.extracting("characterType", "level", "characterProgressStatus")
			.contains(CLOVER, 1, IN_PROGRESS);

		assertThat(response.getCompletedCharacterCount())
			.containsExactlyInAnyOrderEntriesOf(characterCountMap);
	}

	@DisplayName("진행중인 캐릭터정보들과 완료한 캐릭터 개수들을 가져온다. (2)")
	@Test
	void getCharacterSummary_2() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 120);
		LuckkidsCharacter luckkidsCharacter1 = createLuckkidsCharacter(CLOVER, 1,
			"https://test.cloudfront.net/test1.json", "https://test.cloudfront.net/test1.png");
		LuckkidsCharacter luckkidsCharacter5 = createLuckkidsCharacter(CLOUD, 5,
			"https://test.cloudfront.net/test5.json", "https://test.cloudfront.net/test5.png");
		UserCharacter userCharacter1 = createUserCharacter(user, luckkidsCharacter1, IN_PROGRESS);
		UserCharacter userCharacter2 = createUserCharacter(user, luckkidsCharacter5, COMPLETED);

		userRepository.save(user);
		luckkidsCharacterRepository.saveAll(List.of(luckkidsCharacter1, luckkidsCharacter5));
		userCharacterRepository.saveAll(List.of(userCharacter1, userCharacter2));

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		Map<CharacterType, Long> characterCountMap = new EnumMap<>(CharacterType.class);
		for (CharacterType type : CharacterType.values()) {
			characterCountMap.put(type, 0L);
		}
		characterCountMap.put(CLOUD, characterCountMap.get(CLOUD) + 1);

		// when
		UserCharacterSummaryResponse response = userCharacterService.getCharacterSummary();

		// then
		assertThat(response.getInProgressCharacter())
			.extracting("characterType", "level", "characterProgressStatus")
			.contains(CLOVER, 1, IN_PROGRESS);

		assertThat(response.getCompletedCharacterCount())
			.containsExactlyInAnyOrderEntriesOf(characterCountMap);
	}

	private User createUser(String email, String password, SnsType snsType, int missionCount) {
		return User.builder()
			.email(email)
			.password(password)
			.snsType(snsType)
			.missionCount(missionCount)
			.build();
	}

	private LoginUserInfo createLoginUserInfo(int userId) {
		return LoginUserInfo.builder()
			.userId(userId)
			.build();
	}

	private LuckkidsCharacter createLuckkidsCharacter(CharacterType characterType, int level, String lottieFile,
		String imageFile) {
		return LuckkidsCharacter.builder()
			.characterType(characterType)
			.level(level)
			.lottieFile(lottieFile)
			.imageFile(imageFile)
			.build();

	}

	private UserCharacter createUserCharacter(User user, LuckkidsCharacter luckkidsCharacter,
		CharacterProgressStatus characterProgressStatus) {
		return UserCharacter.builder()
			.user(user)
			.luckkidsCharacter(luckkidsCharacter)
			.characterProgressStatus(characterProgressStatus)
			.build();
	}

	private LuckkidsCharacter createCharacter(CharacterType characterType) {
		return LuckkidsCharacter.builder()
			.characterType(characterType)
			.level(1)
			.lottieFile("test.json")
			.imageFile("test.png")
			.build();
	}

}
