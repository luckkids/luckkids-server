package com.luckkids.docs.mail;

import com.luckkids.api.controller.mail.request.SendMailRequest;
import com.luckkids.api.controller.mail.MailController;
import com.luckkids.api.service.mail.MailService;
import com.luckkids.api.service.mail.request.SendMailServiceRequest;
import com.luckkids.api.service.mail.response.SendMailResponse;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MailControllerDocsTest extends RestDocsSupport {

    private final MailService mailService = mock(MailService.class);

    @Override
    protected Object initController() {
        return new MailController(mailService);
    }

    @DisplayName("이메일 인증코드 전송 API")
    @Test
    @WithMockUser(roles = "USER")
    void sendEmail() throws Exception {
        // given
        SendMailRequest request = SendMailRequest.builder()
            .email("tkdrl8908@naver.com")
            .build();

        given(mailService.sendMail(any(SendMailServiceRequest.class)))
            .willReturn(SendMailResponse.builder()
                .authNum("232422")
                .build()
            );

        // when // then
        mockMvc.perform(
                post("/api/v1/mail/send")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("send-mail",
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
                    fieldWithPath("data.authNum").type(JsonFieldType.STRING)
                        .description("인증코드")
                )
            ));
    }
}
