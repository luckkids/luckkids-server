package com.luckkids.api.controller.login;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.login.request.LoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginControllerTest extends ControllerTestSupport {

    @DisplayName("일반 로그인을 한다.")
    @Test
    @WithMockUser(roles = "USER")
    void createMission() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .deviceId("testdeviceId")
            .pushKey("testPushToken")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/auth/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("일반 로그인을 할 시 이메일은 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void createMissionWithoutEmail() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
            .password("1234")
            .deviceId("testdeviceId")
            .pushKey("testPushToken")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/auth/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("이메일은 필수입니다."));
    }

    @DisplayName("일반 로그인을 할 시 비밀번호는 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void createMissionWithoutPassword() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
            .email("tkdrl8908@naver.com")
            .deviceId("testdeviceId")
            .pushKey("testPushToken")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/auth/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("비밀번호는 필수입니다."));
    }

    @DisplayName("일반 로그인을 할 시 디바이스ID는 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void createMissionWithoutDeviceId() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .pushKey("testPushToken")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/auth/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("디바이스ID는 필수입니다."));
    }

    @DisplayName("일반 로그인을 할 시 푸시토큰은 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void createMissionWithoutPushToken() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .deviceId("testdeviceId")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/auth/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("푸시토큰은 필수입니다."));
    }

}