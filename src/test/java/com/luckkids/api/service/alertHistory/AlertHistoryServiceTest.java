package com.luckkids.api.service.alertHistory;

import com.luckkids.IntegrationTestSupport;
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
import org.springframework.transaction.annotation.Transactional;

import static com.luckkids.domain.alertHistory.AlertHistoryStatus.CHECKED;
import static com.luckkids.domain.alertHistory.AlertHistoryStatus.UNCHECKED;
import static org.assertj.core.api.Assertions.assertThat;

class AlertHistoryServiceTest extends IntegrationTestSupport {

    @Autowired
    private AlertHistoryService alertHistoryService;

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

    @DisplayName("알림 내역을 `확인`으로 업데이트 한다.")
    @Test
    @Transactional
    void updateAlertHistoryStatus() {
        // given
        User user = createUser();
        userRepository.save(user);

        Push push = createPush(user);
        pushRepository.save(push);

        AlertHistory alertHistory = createAlertHistory(push, UNCHECKED);
        alertHistoryRepository.save(alertHistory);

        // when
        alertHistoryService.updateAlertHistoryStatus(alertHistory.getId());

        // then
        AlertHistory findedAlertHistory = alertHistoryRepository.getReferenceById(alertHistory.getId());

        assertThat(findedAlertHistory.getAlertHistoryStatus()).isEqualTo(CHECKED);
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

    private AlertHistory createAlertHistory(Push push, AlertHistoryStatus alertHistoryStatus) {
        return AlertHistory.builder()
            .push(push)
            .alertDescription("test")
            .alertHistoryStatus(alertHistoryStatus)
            .build();
    }
}