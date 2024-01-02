package com.luckkids.domain.push;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class PushRepositoryTest extends IntegrationTestSupport {
    @Autowired
    private PushRepository pushRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자의 푸시목록을 삭제한다.")
    void deleteTest(){
        User user = User.builder()
            .email("test@email.com")
            .password("1234")
            .snsType(SnsType.NORMAL)
            .build();

        userRepository.save(user);

        Push push = Push.builder()
            .deviceId("testDevice")
            .pushToken("testPushToken")
            .user(user)
            .build();

        Push savedPush =  pushRepository.save(push);
        pushRepository.deleteAllByUserId(user.getId());

        Optional<Push> findPush = pushRepository.findById(savedPush.getId());

        assertThat(findPush.isEmpty()).isTrue();
    }
}
