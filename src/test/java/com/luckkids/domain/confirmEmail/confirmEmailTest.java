package com.luckkids.domain.confirmEmail;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
public class confirmEmailTest extends IntegrationTestSupport {

    @Autowired
    private ConfirmEmailRepository confirmEmailRepository;

    @Test
    @DisplayName("이메일의 인증완료 여부를 체크한다.")
    void checkEmail() {
        ConfirmEmail confirmEmail = ConfirmEmail.builder()
            .email("test@test.com")
            .authKey("testesttest")
            .confirmStatus(ConfirmStatus.INCOMPLETE)
            .build();

        ConfirmEmail savedConfirmEmail = confirmEmailRepository.save(confirmEmail);

        assertThatThrownBy(savedConfirmEmail::checkEmail)
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("이메일 인증이 완료되지 않았습니다.");
    }

    @Test
    @DisplayName("이메일의 인증완료 여부를 체크한다.")
    void emailConfirm() {
        ConfirmEmail confirmEmail = ConfirmEmail.builder()
            .email("test@test.com")
            .authKey("testesttest")
            .confirmStatus(ConfirmStatus.INCOMPLETE)
            .build();

        ConfirmEmail savedConfirmEmail = confirmEmailRepository.save(confirmEmail);

        savedConfirmEmail.confirm();

        assertThat(confirmEmail.getConfirmStatus()).isEqualTo(ConfirmStatus.COMPLETE);
    }
}
