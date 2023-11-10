package com.luckkids.docs.login;

import com.luckkids.api.controller.login.LoginController;
import com.luckkids.api.controller.login.request.LoginRequest;
import com.luckkids.api.service.login.LoginService;
import com.luckkids.api.service.login.request.LoginServiceRequest;
import com.luckkids.api.service.login.response.LoginResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.user.SettingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginControllerDocsTest extends RestDocsSupport {

    private final LoginService loginService = mock(LoginService.class);

    @Override
    protected Object initController() {
        return new LoginController(loginService);
    }

    @DisplayName("일반 로그인 API")
    @Test
    void createMission() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .deviceId("testdeviceId")
            .pushKey("tessPushKey")
            .build();

        given(loginService.normalLogin(any(LoginServiceRequest.class)))
            .willReturn(LoginResponse.builder()
                .email("tkdrl8908@naver.com")
                .accessToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0a2RybDg5MDhAbmF2ZXIuY29tIiwiZXhwIjoxNjk3NzI1OTYzfQ.StpNeN7Mrcm9n3niSPU8ItRMBZqy__gS8AjRkqlIZ2dWtLaciMQF6EGPY4JaagoFkP-GfhUr8pMYfRewEZ-BYg")
                .refreshToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0a2RybDg5MDhAbmF2ZXIuY29tIiwiZXhwIjoxNjk3NzY5MTYzfQ.DJwKVuZxw3zTK8RdnnwS45JM0V_3DJ0kpCDMaf3wnyv5GwLtwwKtVNhfeJmhcGYJZ3gvu534kAZGtAoZb_dgWw")
                .settingStatus(SettingStatus.INCOMPLETE)
                .build()
            );

        // when // then
        mockMvc.perform(
                post("/api/v1/auth/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("user-login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("이메일"),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("비밀번호"),
                    fieldWithPath("deviceId").type(JsonFieldType.STRING)
                        .description("디바이스ID"),
                    fieldWithPath("pushKey").type(JsonFieldType.STRING)
                        .description("푸시키")
                ),
                responseFields(
                    fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메세지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.email").type(JsonFieldType.STRING)
                        .description("이메일"),
                    fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                        .description("Access-Token"),
                    fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                        .description("Refresh-Token"),
                    fieldWithPath("data.settingStatus").type(JsonFieldType.STRING)
                        .description("미션 및 캐릭터 초기세팅여부")
                )
            ));
    }
}
