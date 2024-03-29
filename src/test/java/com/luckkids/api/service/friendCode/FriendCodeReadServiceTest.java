package com.luckkids.api.service.friendCode;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.friendCode.response.FriendInviteCodeResponse;
import com.luckkids.domain.friendCode.FriendCode;
import com.luckkids.domain.friendCode.FriendCodeRepository;
import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.BDDMockito.given;

public class FriendCodeReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendCodeRepository friendCodeRepository;
    @Autowired
    private FriendCodeReadService friendCodeReadService;

    @AfterEach
    void tearDown(){
        friendCodeRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("FriendCode Entity를 조회한다.")
    @Test
    void findByCode(){
        User user = createUser(1);
        User savedUser = userRepository.save(user);

        FriendCode code = createFriendCode(savedUser);
        friendCodeRepository.save(code);

        FriendCode findCode = friendCodeReadService.findByCode("ABCDEFGH");

    }

    private User createUser(int i) {
        return User.builder()
            .email("test" + i)
            .password("1234")
            .missionCount(i)
            .luckPhrase("행운입니다.")
            .snsType(SnsType.NORMAL)
            .role(Role.USER)
            .build();
    }

    private FriendCode createFriendCode(User user){
        return FriendCode.builder()
            .user(user)
            .code("ABCDEFGH")
            .build();
    }

}
