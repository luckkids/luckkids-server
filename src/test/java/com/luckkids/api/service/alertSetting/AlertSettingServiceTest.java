package com.luckkids.api.service.alertSetting;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.alertSetting.request.AlertSettingUpdateServiceRequest;
import com.luckkids.api.service.alertSetting.response.AlertSettingResponse;
import com.luckkids.api.service.alertSetting.response.AlertSettingUpdateResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.domain.alertSetting.AlertSettingRepository;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.jwt.dto.UserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.luckkids.domain.misson.AlertStatus.CHECKED;
import static com.luckkids.domain.user.SnsType.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

public class AlertSettingServiceTest extends IntegrationTestSupport {
    @Autowired
    private AlertSettingRepository alertSettingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AlertSettingService alertSettingService;
    @Autowired
    private SecurityService securityService;

    @AfterEach
    void tearDown() {
        alertSettingRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("사용자의 알림설정을 수정한다.")
    @Test
    void updateAlertSettingTest(){
        User user = createUser(1);
        createAlertSetting(user);

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(user.getId()));

        AlertSettingUpdateServiceRequest alertSettingUpdateServiceRequest = AlertSettingUpdateServiceRequest.builder()
            .alertStatus(AlertStatus.UNCHECKED)
            .alertType(AlertType.LUCK)
            .build();

        AlertSettingUpdateResponse alertSettingUpdateResponse = alertSettingService.updateAlertSetting(alertSettingUpdateServiceRequest);

        assertThat(alertSettingUpdateResponse).extracting("entire","mission","notice","luck")
            .contains(CHECKED, CHECKED, CHECKED, AlertStatus.UNCHECKED);
    }

    @DisplayName("사용자의 알림설정을 등록한다.")
    @Test
    void createAlertSettingTest(){
        User user = createUser(1);

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(user.getId()));

        AlertSettingResponse alertSettingResponse = alertSettingService.createAlertSetting();

        assertThat(alertSettingResponse).extracting("entire","mission","notice","luck")
            .contains(CHECKED, CHECKED, CHECKED, CHECKED);
    }

    @DisplayName("사용자의 알림설정을 등록시 사용자가 없다면 예외가 발생한다.")
    @Test
    void createAlertSettingWithNoUser(){
        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(1));

        assertThatThrownBy(() -> alertSettingService.createAlertSetting())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 유저는 없습니다. id = " + 1);
    }

    private User createUser(int i) {
        return userRepository.save(
            User.builder()
                .email("test"+i)
                .password("password")
                .snsType(NORMAL)
                .build());
    }

    private void createAlertSetting(User user){
        AlertSetting alertSetting = AlertSetting.builder()
            .user(user)
            .entire(CHECKED)
            .mission(CHECKED)
            .notice(CHECKED)
            .luck(CHECKED)
            .build();

        alertSettingRepository.save(alertSetting);
    }

    private UserInfo createUserInfo(int userId) {
        return UserInfo.builder()
            .userId(userId)
            .email("")
            .build();
    }
}
