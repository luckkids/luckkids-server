package com.luckkids.docs.user;

import com.luckkids.api.controller.user.UserController;
import com.luckkids.api.controller.user.request.UserFindEmailRequest;
import com.luckkids.api.controller.user.request.UserLuckPhrasesRequest;
import com.luckkids.api.controller.user.request.UserUpdatePasswordRequest;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.user.request.UserFindEmailServiceRequest;
import com.luckkids.api.service.user.request.UserLuckPhrasesServiceRequest;
import com.luckkids.api.service.user.request.UserUpdatePasswordServiceRequest;
import com.luckkids.api.service.user.response.UserFindSnsTypeResponse;
import com.luckkids.api.service.user.response.UserLuckPhrasesResponse;
import com.luckkids.api.service.user.response.UserUpdatePasswordResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.user.SnsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerDocsTest extends RestDocsSupport {

    private final UserService userService = mock(UserService.class);
    private final UserReadService userReadService = mock(UserReadService.class);

    @Override
    protected Object initController() {
        return new UserController(userService, userReadService);
    }

    @DisplayName("사용자 프로필 행운문구 수정 API")
    @Test
    void createMission() throws Exception {
        // given
        UserLuckPhrasesRequest request = UserLuckPhrasesRequest.builder()
            .luckPhrases("행운입니다.!")
            .build();

        given(userService.updatePhrase(any(UserLuckPhrasesServiceRequest.class)))
            .willReturn(UserLuckPhrasesResponse.builder()
                .luckPhrases("행운입니다.!")
                .build()
            );

        // when // then
        mockMvc.perform(
                patch("/api/v1/user/phrase")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("update-phrase",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("luckPhrases").type(JsonFieldType.STRING)
                        .description("행운문구")
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
                    fieldWithPath("data.luckPhrases").type(JsonFieldType.STRING)
                        .description("행운문구")
                )
            ));
    }

    @DisplayName("비밀번호 재설정 API")
    @Test
    void changePassword() throws Exception {
        // given
        UserUpdatePasswordRequest request = UserUpdatePasswordRequest.builder()
            .email("test@email.com")
            .password("1234")
            .build();

        given(userService.updatePassword(any(UserUpdatePasswordServiceRequest.class)))
            .willReturn(
                UserUpdatePasswordResponse.builder()
                    .email("test@email.com")
                    .build()
            );

        // when // then
        mockMvc.perform(
                patch("/api/v1/user/password/")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("update-password",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("이메일"),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("비밀번호")
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
                        .description("이메일")
                )
            ));
    }
    @DisplayName("이메일 SNS타입 확인 API")
    @Test
    void findSnsType() throws Exception {
        // given
        UserFindEmailRequest request = UserFindEmailRequest.builder()
            .email("test@email.com")
            .build();

        given(userReadService.findEmail(any(UserFindEmailServiceRequest.class)))
            .willReturn(
                UserFindSnsTypeResponse.builder()
                    .snsType(SnsType.NORMAL)
                    .build()
            );

        // when // then
        mockMvc.perform(
                get("/api/v1/user/findEmail")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("user-findEmail",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("이메일")
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
                    fieldWithPath("data.snsType").type(JsonFieldType.STRING)
                        .description("가입한 SNS형식. 가능한 값: "+ Arrays.toString(SnsType.values()))
                )
            ));
    }
}
