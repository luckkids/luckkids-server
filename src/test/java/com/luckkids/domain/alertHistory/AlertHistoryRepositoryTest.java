package com.luckkids.domain.alertHistory;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
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
public class AlertHistoryRepositoryTest extends IntegrationTestSupport {
    @Autowired
    private AlertHistoryRepository alertHistoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserReadService userReadService;

    @Test
    @DisplayName("사용자의 알림목록을 삭제한다.")
    void deleteTest(){
        User user = User.builder()
            .email("test@email.com")
            .password("1234")
            .snsType(SnsType.NORMAL)
            .build();

        userRepository.save(user);

        AlertHistory alertHistory = AlertHistory.builder()
            .user(user)
            .alertDescription("test")
            .alertHistoryStatus(AlertHistoryStatus.CHECKED)
            .build();

        AlertHistory savedAlertHistory =  alertHistoryRepository.save(alertHistory);

        alertHistoryRepository.deleteAllByUserId(user.getId());

        Optional<AlertHistory> findAlertHistory = alertHistoryRepository.findById(savedAlertHistory.getId());

        assertThat(findAlertHistory.isEmpty()).isTrue();
    }
}