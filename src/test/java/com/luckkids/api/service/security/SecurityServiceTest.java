package com.luckkids.api.service.security;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.jwt.dto.LoginUserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class SecurityServiceTest extends IntegrationTestSupport {

    @DisplayName("현재 로그인된 유저정보를 가져온다.")
    @Test
    void getCurrentLoginUserInfo() {
        // given
        int userId = 1;
        LoginUserInfo loginUserInfo = LoginUserInfo.builder()
            .userId(userId)
            .build();

        given(securityService.getCurrentLoginUserInfo())
            .willReturn(loginUserInfo);

        // when
        LoginUserInfo currentLoginUserInfo = securityService.getCurrentLoginUserInfo();

        // then
        assertThat(currentLoginUserInfo.getUserId()).isEqualTo(userId);
    }

    @DisplayName("텍스트를 암호화한다.")
    @Test
    void encrypt() {
        // given
        String text = "TEST_TEXT";
        String expectedText = "ENCRYPTED_TEXT";
        given(securityService.encrypt(text)).willReturn(expectedText);

        // when
        String encryptedText = securityService.encrypt(text);

        // then
        assertThat(encryptedText).isNotNull();
        assertThat(encryptedText).isNotEqualTo(text);
        assertThat(encryptedText).isEqualTo(expectedText);
    }

    @DisplayName("암호화된 텍스트를 복호화한다.")
    @Test
    void decrypt() {
        // given
        String text = "TEST_TEXT";
        String expectedText = "ENCRYPTED_TEXT";
        given(securityService.encrypt(text)).willReturn(expectedText);
        given(securityService.decrypt(expectedText)).willReturn(text);

        // when
        String decryptedText = securityService.decrypt(expectedText);

        // then
        assertThat(decryptedText).isEqualTo(text);
    }
}