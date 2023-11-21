package com.luckkids.docs.user;

import com.luckkids.api.controller.password.PasswordController;
import com.luckkids.api.controller.password.request.UserChangePasswordRequest;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.user.request.UserChangePasswordServiceRequest;
import com.luckkids.api.service.user.response.UserChangePasswordResponse;
import com.luckkids.docs.RestDocsSupport;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerDocsTest extends RestDocsSupport {

    private final UserService userService = mock(UserService.class);

    @Override
    protected Object initController() {
        return new PasswordController(userService);
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
}
