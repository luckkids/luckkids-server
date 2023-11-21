package com.luckkids.docs.user;

import com.luckkids.api.controller.login.request.LoginRequest;
import com.luckkids.api.controller.user.UserController;
import com.luckkids.api.controller.user.request.UserLuckPhrasesRequest;
import com.luckkids.api.service.login.LoginService;
import com.luckkids.api.service.login.request.LoginServiceRequest;
import com.luckkids.api.service.login.response.LoginResponse;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.user.request.UserLuckPhrasesServiceRequest;
import com.luckkids.api.service.user.response.UserLuckPhrasesResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.user.SettingStatus;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerDocsTest extends RestDocsSupport {

    private final UserService userService = mock(UserService.class);

    @Override
    protected Object initController() {
        return new UserController(userService);
    }

    @DisplayName("사용자 프로필 행운문구 수정 API")
    @Test
    void createMission() throws Exception {
        // given
        UserLuckPhrasesRequest request = UserLuckPhrasesRequest.builder()
            .luckPhrases("행운입니다.!")
            .build();

        given(userService.update(any(UserLuckPhrasesServiceRequest.class)))
            .willReturn(UserLuckPhrasesResponse.builder()
                .luckPhrases("행운입니다.!")
                .build()
            );

        // when // then
        mockMvc.perform(
                patch("/api/v1/user/update")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("user-update",
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

}
