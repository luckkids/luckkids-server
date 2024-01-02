package com.luckkids.docs.withdrawReason;

import com.luckkids.api.controller.withdrawReason.WithdrawReasonController;
import com.luckkids.api.controller.withdrawReason.request.WithdrawReasonCreateRequest;
import com.luckkids.api.service.withdrawReason.WithdrawReasonService;
import com.luckkids.api.service.withdrawReason.request.WithdrawReasonCreateServiceRequest;
import com.luckkids.api.service.withdrawReason.response.WithdrawReasonCreateResponse;
import com.luckkids.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WithdrawReasonControllerDocsTest extends RestDocsSupport {

    private final WithdrawReasonService withdrawReasonService = mock(WithdrawReasonService.class);

    @Override
    protected Object initController() {
        return new WithdrawReasonController(withdrawReasonService);
    }

    @DisplayName("탈퇴사유를 등록하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void createWithdrawReason() throws Exception {
        // given

        WithdrawReasonCreateRequest withdrawReasonCreateRequest = WithdrawReasonCreateRequest.builder()
                .reason("앱을 잘 사용하지 않아요")
                .build();

        given(withdrawReasonService.create(any(WithdrawReasonCreateServiceRequest.class)))
            .willReturn(
                WithdrawReasonCreateResponse.builder()
                    .id(13L)
                    .build()
            );

        // when // then
        mockMvc.perform(
                post("/api/v1/withdraw/reason")
                    .content(objectMapper.writeValueAsString(withdrawReasonCreateRequest))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("withdrawReason-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("reason").type(JsonFieldType.STRING)
                        .description("탈퇴 사유")
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
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                        .description("등록성공한 탈퇴사유 ID")
                )
            ));
    }
}
