package com.luckkids.domain.confirmEmail;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class confirmEmailTest extends IntegrationTestSupport {

    @Test
    @DisplayName("이메일의 인증완료 여부를 체크한다.")
    void checkEmail(){
        ConfirmEmail confirmEmail = ConfirmEmail.builder()
            .email("test@test.com")
            .authKey("testesttest")
            .confirmStatus(ConfirmStatus.INCOMPLETE)
            .build();

        assertThatThrownBy(confirmEmail::checkEmail)
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("인증이 완료되지 않았습니다.");
    }

    @Test
    @DisplayName("이메일의 인증완료 여부를 체크한다.")
    void emailConfirm(){
        ConfirmEmail confirmEmail = ConfirmEmail.builder()
            .email("test@test.com")
            .authKey("testesttest")
            .confirmStatus(ConfirmStatus.INCOMPLETE)
            .build();

        confirmEmail.confirm();

        assertThat(confirmEmail.getConfirmStatus()).isEqualTo(ConfirmStatus.COMPLETE);
    }
}
