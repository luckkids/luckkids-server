package com.luckkids.api.service.userCharacter;

import static com.luckkids.domain.userCharacter.CharacterProgressStatus.*;
import static com.luckkids.domain.userCharacter.Level.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.service.luckkidsCharacter.LuckkidsCharacterReadService;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.userCharacter.request.UserCharacterCreateServiceRequest;
import com.luckkids.api.service.userCharacter.response.UserCharacterCreateResponse;
import com.luckkids.api.service.userCharacter.response.UserCharacterLevelUpResponse;
import com.luckkids.api.service.userCharacter.response.UserCharacterSummaryResponse;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;
import com.luckkids.domain.user.User;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.domain.userCharacter.UserCharacterQueryRepository;
import com.luckkids.domain.userCharacter.UserCharacterRepository;
import com.luckkids.domain.userCharacter.projection.UserCharacterSummaryDto;
import com.luckkids.domain.userCharacter.projection.UserInProgressCharacterDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCharacterService {

	private final UserCharacterRepository userCharacterRepository;
	private final UserCharacterQueryRepository userCharacterQueryRepository;
	private final LuckkidsCharacterRepository luckkidsCharacterRepository;
	private final LuckkidsCharacterReadService luckkidsCharacterReadService;
	private final UserReadService userReadService;
	private final SecurityService securityService;

	public UserCharacterCreateResponse createUserCharacter(UserCharacterCreateServiceRequest request) {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		User user = userReadService.findByOne(userId);

		user.updateNickName(request.getNickName());

		LuckkidsCharacter luckkidsCharacter = luckkidsCharacterReadService.findById(request.getId());

		UserCharacter userCharacter = request.toEntity(user, luckkidsCharacter);
		return UserCharacterCreateResponse.of(user, userCharacterRepository.save(userCharacter));
	}

	public UserCharacterLevelUpResponse determineLevelUp(User user) {
		int level = user.calculateLevelBasedOnRemainingMissions();

		if (level == 0) {
			return UserCharacterLevelUpResponse.of(false, null, 0);
		}

		UserInProgressCharacterDto inProgressCharacters = userCharacterQueryRepository.findInProgressCharacterByUserId(
			user.getId());
		LuckkidsCharacter LevelUpLuckkidsCharacter = luckkidsCharacterRepository.findByCharacterTypeAndLevel(
			inProgressCharacters.luckkidsCharacter().getCharacterType(), level
		);

		return handleCharacterLevelUpUserCharacter(inProgressCharacters.userCharacter(), LevelUpLuckkidsCharacter,
			level);
	}

	public UserCharacterSummaryResponse getCharacterSummary() {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		userReadService.findByOne(userId);

		List<UserCharacterSummaryDto> userCharacterSummaries = userCharacterQueryRepository.findUserCharacterSummary(
			userId);

		UserCharacterSummaryDto inProgressCharacter = findInProgressCharacter(userCharacterSummaries);

		Map<CharacterType, Long> completedCharacterCount = calculateCompletedCharacterCount(userCharacterSummaries);

		return UserCharacterSummaryResponse.of(inProgressCharacter, completedCharacterCount);
	}

	public UserCharacterLevelUpResponse handleCharacterLevelUpUserCharacter(UserCharacter userCharacter,
		LuckkidsCharacter LevelUpLuckkidsCharacter, int level) {
		if (level == LEVEL_MAX.getLevel()) {
			userCharacter.updateCompleteCharacter();
			userCharacterRepository.save(
				withUserCharacter(userCharacter, luckkidsCharacterReadService.findRandomLuckkidsCharacterLevel1()));
		}
		userCharacter.updateLuckkidsCharacter(LevelUpLuckkidsCharacter);
		return UserCharacterLevelUpResponse.of(true, LevelUpLuckkidsCharacter.getCharacterType(),
			LevelUpLuckkidsCharacter.getLevel()
		);
	}

	public UserCharacterSummaryDto findInProgressCharacter(List<UserCharacterSummaryDto> summaries) {
		return summaries.stream()
			.filter(dto -> dto.characterProgressStatus() == IN_PROGRESS)
			.findAny()
			.orElseThrow(() -> new NoSuchElementException("진행 중인 캐릭터가 없습니다."));
	}

	public Map<CharacterType, Long> calculateCompletedCharacterCount(List<UserCharacterSummaryDto> summaries) {
		Map<CharacterType, Long> completedCharacterCount = new EnumMap<>(CharacterType.class);
		for (CharacterType type : CharacterType.values()) {
			completedCharacterCount.put(type, 0L);
		}

		for (UserCharacterSummaryDto summary : summaries) {
			if (summary.characterProgressStatus() == COMPLETED) {
				CharacterType type = summary.characterType();
				completedCharacterCount.put(type, completedCharacterCount.get(type) + 1);
			}
		}

		return completedCharacterCount;
	}

	public UserCharacter withUserCharacter(UserCharacter userCharacter, LuckkidsCharacter luckkidsCharacter) {
		return UserCharacter.builder()
			.user(userCharacter.getUser())
			.luckkidsCharacter(luckkidsCharacter)
			.characterProgressStatus(IN_PROGRESS)
			.build();
	}
}
