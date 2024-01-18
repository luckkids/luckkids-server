package com.luckkids.api.service.userCharacter;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.userCharacter.request.UserCharacterCreateServiceRequest;
import com.luckkids.api.service.userCharacter.response.UserCharacterCreateResponse;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.domain.userCharacter.UserCharacterRepository;
import com.luckkids.jwt.dto.LoginUserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class UserCharacterServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserCharacterRepository userCharacterRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCharacterService userCharacterService;

    @AfterEach
    void tearDown() {
        userCharacterRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("사용자가 선택한 캐릭터를 저장한다.")
    @Test
    void createTest() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);

        userRepository.save(user);

        given(securityService.getCurrentLoginUserInfo())
            .willReturn(createLoginUserInfo(user.getId()));

        UserCharacterCreateServiceRequest userCharacterCreateServiceRequest = UserCharacterCreateServiceRequest.builder()
            .fileName("test.json")
            .characterNickname("럭키즈")
            .build();

        UserCharacterCreateResponse userCharacterCreateResponse = userCharacterService.createUserCharacter(userCharacterCreateServiceRequest);

        // then
        assertThat(userCharacterCreateResponse)
            .extracting("characterNickname", "fileName")
            .contains("럭키즈", "test.json");

        Optional<UserCharacter> userCharacter = userCharacterRepository.findById(userCharacterCreateResponse.getId());

        assertThat(userCharacter)
            .isPresent()
            .hasValueSatisfying(character -> {
                assertThat(character.getCharacterNickname()).isEqualTo("럭키즈");
                assertThat(character.getFile()).isEqualTo("test.json");
            });
    }

    private User createUser(String email, String password, SnsType snsType) {
        return User.builder()
            .email(email)
            .password(password)
            .snsType(snsType)
            .build();
    }

    private LoginUserInfo createLoginUserInfo(int userId) {
        return LoginUserInfo.builder()
            .userId(userId)
            .build();
    }
}
