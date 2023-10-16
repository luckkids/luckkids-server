package com.luckkids.join.docs;

import com.luckkids.AbstractRestDocsTests;
import com.luckkids.api.controller.join.JoinController;
import com.luckkids.api.controller.join.dto.JoinRequest;
import com.luckkids.api.service.join.JoinService;
import com.luckkids.api.service.join.dto.JoinResponse;
import com.luckkids.api.service.join.dto.JoinSendMailResponse;
import com.luckkids.api.service.join.dto.JoinSendMailServiceRequest;
import com.luckkids.api.service.join.dto.JoinServiceRequest;
import com.luckkids.domain.user.SnsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    private JoinService joinService;


    @DisplayName("이메일 인증코드 전송 API")
    @Test
    void sendMail() throws Exception {
        JoinSendMailServiceRequest request = JoinSendMailServiceRequest.builder()
                .email("tkdrl8908@naver.com")
                .build();

        given(joinService.sendMail(any(JoinSendMailServiceRequest.class)))
                .willReturn(JoinSendMailResponse.builder()
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

    @DisplayName("회원가입 API")
    @Test
    void joinUser() throws Exception {
        JoinRequest request = JoinRequest.builder()
                .email("tkdrl8908@naver.com")
                .nickname("상기")
                .password("1234")
                .phoneNumber("01064091048")
                .build();

        given(joinService.joinUser(any(JoinServiceRequest.class)))
                .willReturn(JoinResponse.builder()
                        .email("tkdrl8908@naver.com")
                        .nickname("상기")
                        .password("1234")
                        .phoneNumber("01064091048")
                        .snsType(SnsType.NORMAL)
                        .build()
                );

        mockMvc.perform(
                        post("/api/v1/join/user")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("사용자 별칭"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                                        .description("사용자 전화번호")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                        .description("API상태코드"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                                        .description("API상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("data.password").type(JsonFieldType.STRING)
                                        .description("비밀번호"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                        .description("사용자 별칭"),
                                fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING)
                                        .description("사용자 전화번호"),
                                fieldWithPath("data.snsType").type(JsonFieldType.STRING)
                                        .description("사용자 로그인 타입")
                        )
                ));
    }
}
