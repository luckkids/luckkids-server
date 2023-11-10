package com.luckkids.api.service.alertSetting;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.alertSetting.response.AlertSettingResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.domain.alertSetting.AlertSettingRepository;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.jwt.dto.UserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

public class AlertSettingReadServiceTest extends IntegrationTestSupport {

    @Autowired
    AlertSettingRepository alertSettingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AlertSettingReadService alertSettingReadService;
    @Autowired
    SecurityService securityService;

    @AfterEach
    void tearDown() {
        alertSettingRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("AlertSetting Entity를 조회한다.")
    @Test
    void findOneByUserId(){
        User user = createUser(1);
        createAlertSetting(user);

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(user.getId()));

        AlertSetting alertSetting = alertSettingReadService.findOneByUserId();

        assertThat(alertSetting).extracting("entire","mission","notice","luck")
            .contains(AlertStatus.CHECKED, AlertStatus.CHECKED, AlertStatus.CHECKED, AlertStatus.CHECKED);
    }

    @DisplayName("AlertSetting Entity를 조회시 존재하지 않으면 예외가 발생된다.")
    @Test
    void findOneByUserIdWithNoUser(){
        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(1));

        assertThatThrownBy(() -> alertSettingReadService.findOneByUserId())
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("해당 사용자가 알림설정이 되어있지 않습니다.");
    }

    @DisplayName("사용자의 알림세팅을 조회한다.")
    @Test
    void getAlertSetting(){
        User user = createUser(1);
        createAlertSetting(user);

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(user.getId()));

        AlertSettingResponse alertSettingResponse = alertSettingReadService.getAlertSetting();

        assertThat(alertSettingResponse).extracting("entire","mission","notice","luck")
            .contains(AlertStatus.CHECKED, AlertStatus.CHECKED, AlertStatus.CHECKED, AlertStatus.CHECKED);
    }

    @DisplayName("사용자의 알림세팅이 존재하지 않을 시 예외가 발생한다.")
    @Test
    void getAlertSettingWithNoSetting (){
        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(2));

        assertThatThrownBy(() -> alertSettingReadService.getAlertSetting())
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("해당 사용자가 알림설정이 되어있지 않습니다.");
    }

    private User createUser(int i) {
        return userRepository.save(
            User.builder()
            .email("test"+i)
            .password("password")
            .snsType(SnsType.NORMAL)
            .phoneNumber("01064091048")
            .build());
    }

    private void createAlertSetting(User user){
        AlertSetting alertSetting = AlertSetting.builder()
            .user(user)
            .entire(AlertStatus.CHECKED)
            .mission(AlertStatus.CHECKED)
            .notice(AlertStatus.CHECKED)
            .luck(AlertStatus.CHECKED)
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
