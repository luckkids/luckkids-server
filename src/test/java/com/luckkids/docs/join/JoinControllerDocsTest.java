package com.luckkids.docs.join;

import com.luckkids.api.controller.join.JoinController;
import com.luckkids.api.controller.join.request.JoinCheckEmailRequest;
import com.luckkids.api.controller.join.request.JoinRequest;
import com.luckkids.api.service.join.JoinReadService;
import com.luckkids.api.service.join.JoinService;
import com.luckkids.api.service.join.request.JoinCheckEmailServiceRequest;
import com.luckkids.api.service.join.request.JoinServiceRequest;
import com.luckkids.api.service.join.response.JoinCheckEmailResponse;
import com.luckkids.api.service.join.response.JoinResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.userAgreement.AgreementStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JoinControllerDocsTest extends RestDocsSupport {

    private final JoinService joinService =  mock(JoinService.class);
    private final JoinReadService joinReadService = mock(JoinReadService.class);


    @Override
    protected Object initController() {
        return new JoinController(joinService, joinReadService);
    }

    @DisplayName("이메일 중복체크 API")
    @Test
    @WithMockUser(roles = "USER")
    void checkEmail() throws Exception {
        // given
        JoinCheckEmailRequest request = JoinCheckEmailRequest.builder()
            .email("tkdrl8908@naver.com")
            .build();

        given(joinReadService.checkEmail(any(JoinCheckEmailServiceRequest.class)))
            .willReturn(JoinCheckEmailResponse.builder()
                .email("tkdrl8908@naver.com")
                .build()
            );

        // when // then
        mockMvc.perform(
                post("/api/v1/join/checkEmail")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("check-email",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("중복체크 할 이메일")
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
                        .description("중복 체크 된 이메일")
                )
            ));
    }

    @DisplayName("회원가입 API")
    @Test
    @WithMockUser(roles = "USER")
    void joinUser() throws Exception {
        // given
        JoinRequest request = JoinRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .termUserAgreement(AgreementStatus.AGREE)
            .personalInfoAgreement(AgreementStatus.AGREE)
            .marketingAgreement(AgreementStatus.AGREE)
            .build();

        given(joinService.joinUser(any(JoinServiceRequest.class)))
            .willReturn(JoinResponse.builder()
                .id(1)
                .email("tkdrl8908@naver.com")
                .snsType(SnsType.NORMAL)
                .termUserAgreement(AgreementStatus.AGREE)
                .personalInfoAgreement(AgreementStatus.AGREE)
                .marketingAgreement(AgreementStatus.AGREE)
                .build()
            );

        // when // then
        mockMvc.perform(
                post("/api/v1/join/user")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("join-user",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("이메일"),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("패스워드"),
                    fieldWithPath("termUserAgreement").type(JsonFieldType.STRING)
                            .description("이용약관 필수동의 여부"  + Arrays.toString(AgreementStatus.values())),
                    fieldWithPath("personalInfoAgreement").type(JsonFieldType.STRING)
                            .description("개인정보 동의여부"  + Arrays.toString(AgreementStatus.values())),
                    fieldWithPath("marketingAgreement").type(JsonFieldType.STRING)
                            .description("마케팅 수신여부"  + Arrays.toString(AgreementStatus.values()))
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
                        .description("id"),
                    fieldWithPath("data.email").type(JsonFieldType.STRING)
                        .description("이메일"),
                    fieldWithPath("data.snsType").type(JsonFieldType.STRING)
                        .description("로그인타입. 가능한 값: "+ Arrays.toString(SnsType.values())),
                    fieldWithPath("data.termUserAgreement").type(JsonFieldType.STRING)
                        .description("이용약관 필수동의 여부"  + Arrays.toString(AgreementStatus.values())),
                    fieldWithPath("data.personalInfoAgreement").type(JsonFieldType.STRING)
                        .description("개인정보 동의여부"  + Arrays.toString(AgreementStatus.values())),
                    fieldWithPath("data.marketingAgreement").type(JsonFieldType.STRING)
                        .description("마케팅 수신여부"  + Arrays.toString(AgreementStatus.values()))
                )
            ));
    }
}
