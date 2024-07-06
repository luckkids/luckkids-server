package com.luckkids.api.controller.join;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.join.request.JoinCheckEmailRequest;
import com.luckkids.api.controller.join.request.JoinRequest;
import com.luckkids.domain.userAgreement.AgreementStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JoinControllerTest extends ControllerTestSupport {

    @DisplayName("이메일 중복확인 테스트")
    @Test
    @WithMockUser("USER")
    void checkEmail() throws Exception {
        // given
        JoinCheckEmailRequest request = JoinCheckEmailRequest.builder()
            .email("tkdrl8908@naver.com")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/join/checkEmail")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("회원가입 테스트")
    @Test
    @WithMockUser("USER")
    void joinUser() throws Exception {
        // given
        JoinRequest request = JoinRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .termUserAgreement(AgreementStatus.AGREE)
            .personalInfoAgreement(AgreementStatus.AGREE)
            .marketingAgreement(AgreementStatus.AGREE)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/join/user")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.statusCode").value("201"))
            .andExpect(jsonPath("$.httpStatus").value("CREATED"))
            .andExpect(jsonPath("$.message").value("CREATED"));
    }

    @DisplayName("회원가입시 비밀번호는 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void joinUserWithoutPassword() throws Exception {
        // given
        JoinRequest request = JoinRequest.builder()
            .email("tkdrl8908@naver.com")
            .termUserAgreement(AgreementStatus.AGREE)
            .personalInfoAgreement(AgreementStatus.AGREE)
            .marketingAgreement(AgreementStatus.AGREE)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/join/user")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("비밀번호는 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("회원가입시 이용약관 필수동의 여부는 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void joinUserWithoutTermUserAgreement() throws Exception {
        // given
        JoinRequest request = JoinRequest.builder()
                .email("tkdrl8908@naver.com")
                .password("1234")
                .personalInfoAgreement(AgreementStatus.AGREE)
                .marketingAgreement(AgreementStatus.AGREE)
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/join/user")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이용약관 필수동의여부는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("회원가입시 개인정보 필수동의여부는 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void joinUserWithoutPersonalInfoAgreement() throws Exception {
        // given
        JoinRequest request = JoinRequest.builder()
                .email("tkdrl8908@naver.com")
                .password("1234")
                .termUserAgreement(AgreementStatus.AGREE)
                .marketingAgreement(AgreementStatus.AGREE)
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/join/user")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("개인정보 필수동의여부는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("회원가입시 마케팅 수신동의여부는 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void joinUserWithoutMarketingAgreement() throws Exception {
        // given
        JoinRequest request = JoinRequest.builder()
                .email("tkdrl8908@naver.com")
                .password("1234")
                .termUserAgreement(AgreementStatus.AGREE)
                .personalInfoAgreement(AgreementStatus.AGREE)
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/join/user")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("마케팅 수신동의여부는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
