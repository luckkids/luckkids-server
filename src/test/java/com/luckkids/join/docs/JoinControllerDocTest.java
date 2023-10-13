package com.luckkids.join.docs;

import com.luckkids.AbstractRestDocsTests;
import com.luckkids.api.controller.join.JoinController;
import com.luckkids.api.service.join.JoinService;
import com.luckkids.api.service.join.dto.JoinSendMailServiceRequest;
import com.luckkids.api.service.join.dto.JoinSendMailServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JoinController.class)
public class JoinControllerDocTest extends AbstractRestDocsTests {

    private final JoinService joinService = Mockito.mock(JoinService.class);

    @DisplayName("이메일 인증코드 전송 API")
    @Test
    void sendMail() throws Exception {
        JoinSendMailServiceRequest request = JoinSendMailServiceRequest.builder()
                .email("tkdrl8908@naver.com")
                .build();

        given(joinService.sendMail(any(JoinSendMailServiceRequest.class)))
                .willReturn(JoinSendMailServiceResponse.builder()
                        .code("001")
                        .build()
                );

        mockMvc.perform(
                        post("/api/v1/join/sendEmail")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("받을 이메일")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("인증코드")
                        )
                ));
    }

}
