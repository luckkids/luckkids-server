package com.luckkids.domain.confirmEmail;

import com.luckkids.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class ConfirmEmailRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ConfirmEmailRepository confirmEmailRepository;

    @Test
    @DisplayName("이메일로 ConfirmEmail테이블을 조회한다.")
    void findEmail(){
        ConfirmEmail confirmEmail = ConfirmEmail.builder()
            .email("test@test.com")
            .authKey("Asdasdasdasd")
            .confirmStatus(ConfirmStatus.COMPLETE)
            .build();

        confirmEmailRepository.save(confirmEmail);

        ConfirmEmail findConfirmEmail = confirmEmailRepository.findByEmail("test@test.com").get();

        assertThat(findConfirmEmail)
            .extracting("email", "authKey", "confirmStatus")
            .contains("test@test.com", "Asdasdasdasd", ConfirmStatus.COMPLETE);
    }

    @Test
    @DisplayName("이메일로 ConfirmEmail테이블을 조회한다.")
    void findEmailAndAuthKey(){
        ConfirmEmail confirmEmail = ConfirmEmail.builder()
            .email("test@test.com")
            .authKey("Asdasdasdasd")
            .confirmStatus(ConfirmStatus.COMPLETE)
            .build();

        confirmEmailRepository.save(confirmEmail);

        ConfirmEmail findConfirmEmail = confirmEmailRepository.findByEmailAndAuthKey("test@test.com", "Asdasdasdasd").get();

        assertThat(findConfirmEmail)
            .extracting("email", "authKey", "confirmStatus")
            .contains("test@test.com", "Asdasdasdasd", ConfirmStatus.COMPLETE);
    }

    @Test
    @DisplayName("이메일로 이메일인증테이블 데이터를 모두 제거한다.")
    void deleteAllByEmail(){
        ConfirmEmail confirmEmail = ConfirmEmail.builder()
            .email("test@test.com")
            .authKey("Asdasdasdasd")
            .confirmStatus(ConfirmStatus.COMPLETE)
            .build();

        confirmEmailRepository.save(confirmEmail);

        confirmEmailRepository.deleteAllByEmail("test@test.com");

        List<ConfirmEmail> confirmEmailList = confirmEmailRepository.findAll();

        assertThat(confirmEmailList).hasSize(0);
    }
}
