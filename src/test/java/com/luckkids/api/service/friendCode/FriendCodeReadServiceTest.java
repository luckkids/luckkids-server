package com.luckkids.api.service.friendCode;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.alertHistory.AlertHistoryRepository;
import com.luckkids.domain.friend.Friend;
import com.luckkids.domain.friend.FriendRepository;
import com.luckkids.domain.friendCode.FriendCode;
import com.luckkids.domain.friendCode.FriendCodeRepository;
import com.luckkids.domain.friendCode.UseStatus;
import com.luckkids.domain.user.*;
import com.luckkids.jwt.dto.LoginUserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class FriendCodeReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendCodeRepository friendCodeRepository;
    @Autowired
    private FriendCodeReadService friendCodeReadService;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private AlertHistoryRepository alertHistoryRepository;

    @AfterEach
    void tearDown() {
        alertHistoryRepository.deleteAllInBatch();
        friendCodeRepository.deleteAllInBatch();
        friendRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("FriendCode Entity를 조회한다.")
    @Test
    void findByCode() {
        User user = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
        User savedUser = userRepository.save(user);

        FriendCode code = createFriendCode(savedUser);
        friendCodeRepository.save(code);

        FriendCode findCode = friendCodeReadService.findByCode("ABCDEFGH");
        assertThat(findCode).extracting("code", "useStatus")
                .contains("ABCDEFGH", UseStatus.UNUSED);
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


    private FriendCode createFriendCode(User user) {
        return FriendCode.builder()
                .user(user)
                .code("ABCDEFGH")
                .useStatus(UseStatus.UNUSED)
                .build();
    }


}
