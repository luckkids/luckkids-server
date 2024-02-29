package com.luckkids.api.service.alertHistory;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.alertHistory.request.AlertHistoryDeviceIdServiceRequest;
import com.luckkids.api.service.alertHistory.response.AlertHistoryResponse;
import com.luckkids.domain.alertHistory.AlertHistory;
import com.luckkids.domain.alertHistory.AlertHistoryRepository;
import com.luckkids.domain.alertHistory.AlertHistoryStatus;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class AlertHistoryReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private AlertHistoryReadService alertHistoryReadService;

    @Autowired
    private AlertHistoryRepository alertHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PushRepository pushRepository;

    @AfterEach
    void tearDown() {
        alertHistoryRepository.deleteAllInBatch();
        pushRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("알림 내역을 가져온다.")
    @Test
    void getAlertHistory() {
        // given
        User user = createUser();
        userRepository.save(user);

        Push push = createPush(user);
        pushRepository.save(push);

        AlertHistory alertHistory = createAlertHistory(push);
        AlertHistory savedAlertHistory = alertHistoryRepository.save(alertHistory);
        AlertHistoryResponse response = AlertHistoryResponse.of(savedAlertHistory);

        AlertHistoryDeviceIdServiceRequest request = AlertHistoryDeviceIdServiceRequest.builder()
            .deviceId(push.getDeviceId())
            .build();

        // when
        AlertHistoryResponse result = alertHistoryReadService.getAlertHistory(request);

        // then
        assertThat(result).extracting("id", "alertDescription", "alertHistoryStatus")
            .contains(response.getId(), response.getAlertDescription(), response.getAlertHistoryStatus());
    }


    private User createUser() {
        return User.builder()
            .email("test@email.com")
            .password("1234")
            .snsType(SnsType.NORMAL)
            .build();
    }

    private Push createPush(User user) {
        return Push.builder()
            .user(user)
            .deviceId("testdeviceId")
            .pushToken("testPushToken")
            .build();
    }

    private AlertHistory createAlertHistory(Push push) {
        return AlertHistory.builder()
            .push(push)
            .alertDescription("test")
            .alertHistoryStatus(AlertHistoryStatus.CHECKED)
            .build();
    }
}