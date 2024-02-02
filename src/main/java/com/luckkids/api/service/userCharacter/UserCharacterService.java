package com.luckkids.api.service.userCharacter;

import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.userCharacter.request.UserCharacterCreateServiceRequest;
import com.luckkids.api.service.userCharacter.response.UserCharacterCreateResponse;
import com.luckkids.api.service.userCharacter.response.UserCharacterLevelUpResponse;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;
import com.luckkids.domain.user.User;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.domain.userCharacter.UserCharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.luckkids.domain.userCharacter.CharacterProgressStatus.IN_PROGRESS;
import static com.luckkids.domain.userCharacter.Level.LEVEL_MAX;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCharacterService {

    private final UserCharacterRepository userCharacterRepository;
    private final LuckkidsCharacterRepository luckkidsCharacterRepository;

    private final UserReadService userReadService;
    private final SecurityService securityService;

    public UserCharacterCreateResponse createUserCharacter(UserCharacterCreateServiceRequest request) {
        int userId = securityService.getCurrentLoginUserInfo().getUserId();
        User user = userReadService.findByOne(userId);

        UserCharacter userCharacter = request.toEntity(user);
        return UserCharacterCreateResponse.of(userCharacterRepository.save(userCharacter));
    }

    public UserCharacterLevelUpResponse determineLevelUp(User user) {
        int level = user.calculateLevelBasedOnRemainingMissions();

        if (level == 0) {
            return UserCharacterLevelUpResponse.of(false, null, null);
        }

        UserCharacter userCharacter = userCharacterRepository.findByCharacterProgressStatus(IN_PROGRESS);
        LuckkidsCharacter LevelUpLuckkidsCharacter = luckkidsCharacterRepository.findByCharacterTypeAndLevel(
            userCharacter.getLuckkidsCharacter().getCharacterType(), level
        );

        return handleCharacterLevelUpUserCharacter(userCharacter, LevelUpLuckkidsCharacter, level);
    }

    private UserCharacterLevelUpResponse handleCharacterLevelUpUserCharacter(UserCharacter userCharacter, LuckkidsCharacter LevelUpLuckkidsCharacter, int level) {
        if (level == LEVEL_MAX.getLevel()) {
            userCharacter.updateCompleteCharacter();
            // createRandomLevelOneCharacters(); // TODO: 추후 로직 추가 예정 create random 1_level 캐릭터들 로직 생성시 추가 ! ⭐️
        }
        userCharacter.updateLuckkidsCharacter(LevelUpLuckkidsCharacter);
        return UserCharacterLevelUpResponse.of(true, LevelUpLuckkidsCharacter.getLottieFile(), LevelUpLuckkidsCharacter.getImageFile());
    }
}
