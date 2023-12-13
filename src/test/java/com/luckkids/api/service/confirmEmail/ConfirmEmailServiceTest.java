package com.luckkids.api.service.confirmEmail;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.component.Aes256Component;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.confirmEmail.request.ConfirmEmailCheckServiceRequest;
import com.luckkids.api.service.confirmEmail.request.CreateConfrimEmailServiceRequest;
import com.luckkids.api.service.confirmEmail.response.ConfirmEmailCheckResponse;
import com.luckkids.domain.confirmEmail.ConfirmEmail;
import com.luckkids.domain.confirmEmail.ConfirmEmailRepository;
import com.luckkids.domain.confirmEmail.ConfirmStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConfirmEmailServiceTest extends IntegrationTestSupport {

    @Autowired
    private ConfirmEmailService confirmEmailService;
    @Autowired
    private ConfirmEmailRepository confirmEmailRepository;
    @Autowired
    private ConfirmEmailReadService confirmEmailReadService;
    @Autowired
    private Aes256Component aes256Component;

    @AfterEach
    void tearDown() {
        confirmEmailRepository.deleteAllInBatch();
    }

    @DisplayName("이메일 인증여부를 확인한다.")
    @Test
    void checkEmail(){
        ConfirmEmail confirmEmail = createConfirmEmail("test@test.com", "testtesttest", ConfirmStatus.COMPLETE);

        ConfirmEmail savedConfirmEmail =  confirmEmailRepository.save(confirmEmail);

        ConfirmEmailCheckServiceRequest confirmEmailCheckServiceRequest = ConfirmEmailCheckServiceRequest.builder()
            .email(savedConfirmEmail.getEmail())
            .authKey(savedConfirmEmail.getAuthKey())
            .build();

        ConfirmEmailCheckResponse confirmEmailCheckResponse = confirmEmailService.checkEmail(confirmEmailCheckServiceRequest);

        assertThat(confirmEmailCheckResponse.getEmail()).isEqualTo(savedConfirmEmail.getEmail());
    }

    @DisplayName("이메일 인증여부를 확인할 시 인증이 완료되지 않았을 시 예외를 던진다.")
    @Test
    void checkEmailThrowExecption(){
        ConfirmEmail confirmEmail = createConfirmEmail("test@test.com", "testtesttest", ConfirmStatus.INCOMPLETE);

        ConfirmEmail savedConfirmEmail =  confirmEmailRepository.save(confirmEmail);

        ConfirmEmailCheckServiceRequest confirmEmailCheckServiceRequest = ConfirmEmailCheckServiceRequest.builder()
            .email(savedConfirmEmail.getEmail())
            .authKey(savedConfirmEmail.getAuthKey())
            .build();

        assertThatThrownBy(() -> confirmEmailService.checkEmail(confirmEmailCheckServiceRequest))
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("이메일 인증이 완료되지 않았습니다.");
    }

    @DisplayName("이메일 인증을 처리한다.")
    @Test
    void confirmEmail(){
        ConfirmEmail confirmEmail = createConfirmEmail("test@test.com", "testtesttest", ConfirmStatus.INCOMPLETE);

        ConfirmEmail savedConfirmEmail =  confirmEmailRepository.save(confirmEmail);

        confirmEmailService.confirmEmail(aes256Component.encrypt(savedConfirmEmail.getEmail()+"/"+savedConfirmEmail.getAuthKey()));

        ConfirmEmail findConfirmEmail = confirmEmailReadService.findByEmailAndAuthKey(savedConfirmEmail.getEmail(), savedConfirmEmail.getAuthKey());

        assertThat(findConfirmEmail.getConfirmStatus()).isEqualTo(ConfirmStatus.COMPLETE);
    }

    @DisplayName("이메일인증값을 저장한다.")
    @Test
    void createConfirmEmail(){
        CreateConfrimEmailServiceRequest createConfrimEmailServiceRequest = CreateConfrimEmailServiceRequest.builder()
            .email("test@test.com")
            .authKey("testtesttest")
            .build();

        confirmEmailService.createConfirmEmail(createConfrimEmailServiceRequest);

        ConfirmEmail confirmEmail = confirmEmailReadService.findByEmailAndAuthKey("test@test.com", "testtesttest");

        assertThat(confirmEmail)
            .extracting("email", "authKey", "confirmStatus")
            .contains("test@test.com", "testtesttest", ConfirmStatus.INCOMPLETE);
    }

    private ConfirmEmail createConfirmEmail(String email, String authKey, ConfirmStatus confirmStatus){
        return ConfirmEmail.builder()
            .email(email)
            .authKey(authKey)
            .confirmStatus(confirmStatus)
            .build();
    }
}
