package com.luckkids.docs.password;

import com.luckkids.api.controller.password.PasswordController;
import com.luckkids.api.controller.password.request.UserChangePasswordRequest;
import com.luckkids.api.controller.password.request.UserFindSnsTypeRequest;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.user.request.UserChangePasswordServiceRequest;
import com.luckkids.api.service.user.request.UserFindSnsTypeServiceRequest;
import com.luckkids.api.service.user.response.UserChangePasswordResponse;
import com.luckkids.api.service.user.response.UserFindSnsTypeResponse;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PasswordControllerDocsTest extends RestDocsSupport {
    private final UserService userService = mock(UserService.class);
    private final UserReadService userReadService = mock(UserReadService.class);

    @Override
    protected Object initController() {
        return new PasswordController(userService, userReadService);
    }

    @DisplayName("비밀번호 재설정 API")
    @Test
    void changePassword() throws Exception {
        // given
        UserChangePasswordRequest request = UserChangePasswordRequest.builder()
            .email("test@email.com")
            .password("1234")
            .build();

        given(userService.changePassword(any(UserChangePasswordServiceRequest.class)))
            .willReturn(
                UserChangePasswordResponse.builder()
                    .email("test@email.com")
                    .build()
            );

        // when // then
        mockMvc.perform(
                patch("/api/v1/password/update")
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
        UserFindSnsTypeRequest request = UserFindSnsTypeRequest.builder()
            .email("test@email.com")
            .build();

        given(userReadService.findSnsType(any(UserFindSnsTypeServiceRequest.class)))
            .willReturn(
                UserFindSnsTypeResponse.builder()
                    .snsType(SnsType.NORMAL)
                    .build()
            );

        // when // then
        mockMvc.perform(
                get("/api/v1/password/checkEmail")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("checkEmail-SnsType",
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
