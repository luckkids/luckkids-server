package com.luckkids.docs.confirmEmail;

import com.luckkids.api.controller.alertSetting.request.AlertSettingRequest;
import com.luckkids.api.controller.confirmEmail.ConfirmEmailController;
import com.luckkids.api.controller.confirmEmail.request.ConfirmEmailCheckRequest;
import com.luckkids.api.service.alertSetting.request.AlertSettingServiceRequest;
import com.luckkids.api.service.alertSetting.response.AlertSettingResponse;
import com.luckkids.api.service.confirmEmail.ConfirmEmailService;
import com.luckkids.api.service.confirmEmail.request.ConfirmEmailCheckServiceRequest;
import com.luckkids.api.service.confirmEmail.response.ConfirmEmailCheckResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.misson.AlertStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ConfirmEmailControllerDocsTest extends RestDocsSupport {
    private final ConfirmEmailService confirmEmailService = mock(ConfirmEmailService.class);

    @Override
    protected Object initController() {
        return new ConfirmEmailController(confirmEmailService);
    }

    @DisplayName("이메일인증여부확인 API")
    @Test
    @WithMockUser(roles = "USER")
    void checkEmail() throws Exception {
        // given
        ConfirmEmailCheckRequest request = ConfirmEmailCheckRequest.builder()
            .email("test@test.com")
            .authKey("testtesttest")
            .build();

        given(confirmEmailService.checkEmail(any(ConfirmEmailCheckServiceRequest.class)))
            .willReturn(
                ConfirmEmailCheckResponse.builder()
                    .email("test@test.com")
                    .build()
            );

        // when // then
        mockMvc.perform(
                post("/api/v1/confirmEmail/check")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("confirmEmail-check",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
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
                        .description("인증완료 이메일")
                )
            ));
    }

}
