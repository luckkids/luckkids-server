package com.luckkids.docs.fortuneTestHistory;

import com.luckkids.api.controller.fortunTestHistory.FortuneTestHistoryController;
import com.luckkids.api.controller.fortunTestHistory.request.FortuneTestHistoryCreateRequest;
import com.luckkids.api.service.fortuneTestHistory.FortuneTestHistoryReadService;
import com.luckkids.api.service.fortuneTestHistory.FortuneTestHistoryService;
import com.luckkids.api.service.fortuneTestHistory.request.FortuneTestHistoryCreateServiceRequest;
import com.luckkids.api.service.fortuneTestHistory.response.FortuneTestHistoryResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.fortuneTestHistory.FortuneTestResultType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FortuneTestHistoryControllerDocsTest extends RestDocsSupport {

    private final FortuneTestHistoryService fortuneTestHistoryService = mock(FortuneTestHistoryService.class);
    private final FortuneTestHistoryReadService fortuneTestHistoryReadService = mock(
            FortuneTestHistoryReadService.class);

    @Override
    protected Object initController() {
        return new FortuneTestHistoryController(fortuneTestHistoryService, fortuneTestHistoryReadService);
    }

    @DisplayName("운세 테스트 결과를 저장하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void createFortuneTestHistory() throws Exception {
        // given
        FortuneTestHistoryCreateRequest request = FortuneTestHistoryCreateRequest.builder()
                .uuid("럭키즈")
                .resultType(FortuneTestResultType.TOKKINGI)
                .build();

        given(fortuneTestHistoryService.createFortuneTestHistory(any(FortuneTestHistoryCreateServiceRequest.class)))
                .willReturn(FortuneTestHistoryResponse.builder()
                        .id(1)
                        .uuid("럭키즈")
                        .resultType(FortuneTestResultType.TOKKINGI)
                        .build());

        // when // then
        mockMvc.perform(
                        post("/api/v1/fortuneTest")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("fortune-test-history-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("uuid").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("resultType").type(JsonFieldType.STRING)
                                        .description("운세 결과 타입. 가능한 값: " + Arrays.toString(FortuneTestResultType.values()))
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터")
                        )
                                .andWithPrefix("data.", fortuneTestHistoryResponseFields())
                ));
    }

    @DisplayName("운세 테스트를 조회하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void getFortuneTestHistories() throws Exception {
        // given
        String testUuid = "행운의유저";
        given(fortuneTestHistoryReadService.findByUuid(testUuid))
                .willReturn(FortuneTestHistoryResponse.builder()
                        .id(2)
                        .uuid("행운의유저")
                        .resultType(FortuneTestResultType.TAEYANGI)
                        .build()
                );

        // when // then
        mockMvc.perform(
                        get("/api/v1/fortuneTest")
                                .param("uuid", testUuid)  // ← 이게 핵심!
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("fortune-test-history-list",
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
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("운세 테스트 히스토리 ID"),
                                fieldWithPath("data.uuid").type(JsonFieldType.STRING)
                                        .description("uuid"),
                                fieldWithPath("data.resultType").type(JsonFieldType.STRING)
                                        .description("운세 결과 타입")
                        )
                ));
    }

    private FieldDescriptor[] fortuneTestHistoryResponseFields() {
        return new FieldDescriptor[]{
                fieldWithPath("id").type(JsonFieldType.NUMBER)
                        .description("운세 테스트 히스토리 ID"),
                fieldWithPath("uuid").type(JsonFieldType.STRING)
                        .description("uuid"),
                fieldWithPath("resultType").type(JsonFieldType.STRING)
                        .description("운세 결과 타입")
        };
    }
}
