package com.luckkids.api.service.firebase;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.firebase.request.SendPushServiceRequest;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.user.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class FirebaseServiceTest extends IntegrationTestSupport {

    @MockBean
    private FirebaseService firebaseService;
    @Autowired
    private PushRepository pushRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        pushRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("푸시를 발송한다.")
    @Test
    void sendPushTest(){
        User user = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
        userRepository.save(user);

        Push push = createPush(user);
        pushRepository.save(push);

        SendPushServiceRequest sendPushServiceRequest = SendPushServiceRequest.builder()
            .push(push)
            .body("테스트Body")
            .screenName("테스트 screenName")
            .sound("테스트 sound")
            .build();

        firebaseService.sendPushNotification(sendPushServiceRequest);
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
