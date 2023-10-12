package com.luckkids.domain.misson;

import com.luckkids.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static com.luckkids.domain.misson.AlertStatus.CHECKED;
import static com.luckkids.domain.misson.MissionStatus.AWAITING;
import static com.luckkids.domain.user.SnsType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;

class MissionTest {

    @DisplayName("미션 생성 시 미션 상태는 AWAITING이다.")
    @Test
    void test() {
        // given
        User user = User.builder()
            .email("test@naver.com")
            .password("test1234!")
            .snsType(KAKAO)
            .phoneNumber("010-1111-1111")
            .build();

        // when
        Mission mission = Mission.create(user, "TEST_미션", CHECKED, LocalTime.of(9, 0));

        // then
        assertThat(mission.getMissionStatus()).isEqualByComparingTo(AWAITING);
    }
}