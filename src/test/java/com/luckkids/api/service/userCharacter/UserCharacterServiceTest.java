package com.luckkids.api.service.userCharacter;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.userCharacter.request.UserCharacterCreateServiceRequest;
import com.luckkids.api.service.userCharacter.response.UserCharacterCreateResponse;
import com.luckkids.api.service.userCharacter.response.UserCharacterLevelUpResponse;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.luckkids.domain.luckkidsCharacter.CharacterType.CLOVER;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.COMPLETED;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class UserCharacterServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserCharacterService userCharacterService;

    @Autowired
    private UserCharacterRepository userCharacterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LuckkidsCharacterRepository luckkidsCharacterRepository;

    @AfterEach
    void tearDown() {
        userCharacterRepository.deleteAllInBatch();
        luckkidsCharacterRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("사용자가 선택한 캐릭터를 저장한다.")
    @Test
    void createUserCharacter() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 0);

        userRepository.save(user);

        given(securityService.getCurrentLoginUserInfo())
            .willReturn(createLoginUserInfo(user.getId()));

        UserCharacterCreateServiceRequest userCharacterCreateServiceRequest = UserCharacterCreateServiceRequest.builder()
            .fileName("test.json")
            .build();

        UserCharacterCreateResponse userCharacterCreateResponse = userCharacterService.createUserCharacter(userCharacterCreateServiceRequest);

        // then
        assertThat(userCharacterCreateResponse.getFileName()).isEqualTo("test.json");

        Optional<UserCharacter> userCharacter = userCharacterRepository.findById(userCharacterCreateResponse.getId());

        assertThat(userCharacter)
            .isPresent()
            .hasValueSatisfying(character -> {
//                assertThat(character.getFile()).isEqualTo("test.json");   ⭐️
            });
    }

    @DisplayName("레벨업여부를 결정한다. 레벨업 O")
    @Test
    void determineLevelUpTrue() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 20);
        LuckkidsCharacter luckkidsCharacter1 = createLuckkidsCharacter(CLOVER, 0, "https://test.cloudfront.net/test0.json", "https://test.cloudfront.net/test0.png");
        LuckkidsCharacter luckkidsCharacter2 = createLuckkidsCharacter(CLOVER, 1, "https://test.cloudfront.net/test1.json", "https://test.cloudfront.net/test1.png");
        UserCharacter userCharacter = createUserCharacter(user, luckkidsCharacter1, IN_PROGRESS);

        userRepository.save(user);
        luckkidsCharacterRepository.saveAll(List.of(luckkidsCharacter1, luckkidsCharacter2));
        userCharacterRepository.save(userCharacter);

        // when
        UserCharacterLevelUpResponse response = userCharacterService.determineLevelUp(user);

        // then
        assertThat(response)
            .extracting("levelUpResult", "lottieFile", "imageFile")
            .contains(true, "https://test.cloudfront.net/test1.json", "https://test.cloudfront.net/test1.png");
    }

    @DisplayName("레벨업여부를 결정한다. 레벨업 X")
    @Test
    void determineLevelUpFalse() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 15);
        LuckkidsCharacter luckkidsCharacter1 = createLuckkidsCharacter(CLOVER, 0, "https://test.cloudfront.net/test0.json", "https://test.cloudfront.net/test0.png");
        LuckkidsCharacter luckkidsCharacter2 = createLuckkidsCharacter(CLOVER, 1, "https://test.cloudfront.net/test1.json", "https://test.cloudfront.net/test1.png");
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
        LuckkidsCharacter luckkidsCharacter1 = createLuckkidsCharacter(CLOVER, 4, "https://test.cloudfront.net/test4.json", "https://test.cloudfront.net/test4.png");
        LuckkidsCharacter luckkidsCharacter2 = createLuckkidsCharacter(CLOVER, 5, "https://test.cloudfront.net/test5.json", "https://test.cloudfront.net/test5.png");
        UserCharacter userCharacter = createUserCharacter(user, luckkidsCharacter1, IN_PROGRESS);

        userRepository.save(user);
        luckkidsCharacterRepository.saveAll(List.of(luckkidsCharacter1, luckkidsCharacter2));
        userCharacterRepository.save(userCharacter);

        // when
        UserCharacterLevelUpResponse response = userCharacterService.determineLevelUp(user);

        // then
        assertThat(response)
            .extracting("levelUpResult", "lottieFile", "imageFile")
            .contains(true, "https://test.cloudfront.net/test5.json", "https://test.cloudfront.net/test5.png");

        UserCharacter findUserCharacter = userCharacterRepository.getReferenceById(userCharacter.getId());
        assertThat(findUserCharacter.getCharacterProgressStatus()).isEqualTo(COMPLETED);
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

    private LuckkidsCharacter createLuckkidsCharacter(CharacterType characterType, int level, String lottieFile, String imageFile) {
        return LuckkidsCharacter.builder()
            .characterType(characterType)
            .level(level)
            .lottieFile(lottieFile)
            .imageFile(imageFile)
            .build();

    }

    private UserCharacter createUserCharacter(User user, LuckkidsCharacter luckkidsCharacter, CharacterProgressStatus characterProgressStatus) {
        return UserCharacter.builder()
            .user(user)
            .luckkidsCharacter(luckkidsCharacter)
            .characterProgressStatus(characterProgressStatus)
            .build();
    }
}
