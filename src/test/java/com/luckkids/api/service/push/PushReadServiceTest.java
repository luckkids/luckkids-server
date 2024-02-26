package com.luckkids.api.service.push;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.user.*;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;

public class PushReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private PushReadService pushReadService;
    @Autowired
    private PushRepository pushRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        pushRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    void findAllTest(){
        User user = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
        userRepository.save(user);

        Push push = createPush(user);
        pushRepository.save(push);
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
    private Push createPush(User user){
        return Push.builder()
            .user(user)
            .deviceId("testDeviceId")
            .pushToken("testPushToken")
            .build();
    }

}
