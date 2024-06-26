package com.luckkids.api.controller.login;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.login.request.LoginGenerateTokenRequest;
import com.luckkids.api.controller.login.request.LoginOauthRequest;
import com.luckkids.api.controller.login.request.LoginRequest;
import com.luckkids.domain.user.SnsType;
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
    void normalLogin() throws Exception {
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
    void normalLoginWithoutEmail() throws Exception {
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
    void normalLoginWithoutPassword() throws Exception {
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
    void normalLoginWithoutDeviceId() throws Exception {
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

    @DisplayName("엑세스토큰을 재생성한다.")
    @Test
    @WithMockUser(roles = "USER")
    void generateAccessToken() throws Exception {
        // given
        LoginGenerateTokenRequest loginGenerateTokenRequest = LoginGenerateTokenRequest.builder()
            .email("tkdrl8908@naver.com")
            .deviceId("testDeviceId")
            .refreshToken("testRefreshToken")
            .build();

        mockMvc.perform(
        post("/api/v1/auth/refresh")
            .content(objectMapper.writeValueAsString(loginGenerateTokenRequest))
            .contentType(APPLICATION_JSON)
            .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("OAuth로그인을 한다.")
    @Test
    @WithMockUser(roles = "USER")
    void OauthLoginTest() throws Exception {
        // given
        LoginOauthRequest request = LoginOauthRequest.builder()
            .token("testToken")
            .snsType(SnsType.APPLE)
            .pushKey("testPushKey")
            .deviceId("testdeviceId")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/auth/oauth/login")
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

    @DisplayName("엑세스토큰을 재생성시 이메일은 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void generateAccessTokenWithoutEmail() throws Exception {
        // given
        LoginGenerateTokenRequest loginGenerateTokenRequest = LoginGenerateTokenRequest.builder()
            .deviceId("testDeviceId")
            .refreshToken("testRefreshToken")
            .build();

        mockMvc.perform(
        post("/api/v1/auth/refresh")
            .content(objectMapper.writeValueAsString(loginGenerateTokenRequest))
            .contentType(APPLICATION_JSON)
            .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("이메일은 필수입니다."));
    }

    @DisplayName("OAuth로그인을 할 시 snsType값은 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void OauthLoginWithoutTokenTest() throws Exception {
        // given
        LoginOauthRequest request = LoginOauthRequest.builder()
            .token("testToken")
            .pushKey("testPushKey")
            .deviceId("testdeviceId")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/auth/oauth/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("인증타입은 필수입니다."));
    }

    @DisplayName("엑세스토큰을 재생성할시 디바이스ID는 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void generateAccessTokenWithoutDeviceId() throws Exception {
        // given
        LoginGenerateTokenRequest loginGenerateTokenRequest = LoginGenerateTokenRequest.builder()
            .email("tkdrl8908@naver.com")
            .refreshToken("testRefreshToken")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/refresh")
                    .content(objectMapper.writeValueAsString(loginGenerateTokenRequest))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("디바이스ID는 필수입니다."));
    }

    @DisplayName("OAuth로그인을 할 시 token값은 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void OauthLoginWithoutSnsTypeTest() throws Exception {
        // given
        LoginOauthRequest request = LoginOauthRequest.builder()
            .snsType(SnsType.APPLE)
            .pushKey("testPushKey")
            .deviceId("testdeviceId")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/auth/oauth/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Token은 필수입니다."));
    }

    @DisplayName("OAuth로그인을 할 시 DeviceId값은 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void OauthLoginWithoutDeviceIdTest() throws Exception {
        // given
        LoginOauthRequest request = LoginOauthRequest.builder()
            .token("testToken")
            .snsType(SnsType.APPLE)
            .pushKey("testPushKey")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/auth/oauth/login")
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

    @DisplayName("엑세스토큰을 재생성할 시 리플래시토큰은 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void generateAccessTokenWithoutRefreshToken() throws Exception {
        // given
        LoginGenerateTokenRequest loginGenerateTokenRequest = LoginGenerateTokenRequest.builder()
            .email("tkdrl8908@naver.com")
            .deviceId("testDeviceId")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/auth/refresh")
                    .content(objectMapper.writeValueAsString(loginGenerateTokenRequest))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("리플래시토큰은 필수입니다."));
    }
}
