package com.luckkids.api.controller.mail;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.mail.request.SendMailRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MailControllerTest extends ControllerTestSupport {

    @DisplayName("이메일 전송 테스트")
    @Test
    @WithMockUser(roles = "USER")
    void sendMail() throws Exception {
        // given
        SendMailRequest request = SendMailRequest.builder()
            .email("tkdrl8908@naver.com")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/mail/send")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("이메일은 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void sendMailWithoutEmail() throws Exception {
        // given
        SendMailRequest request = SendMailRequest.builder()
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/mail/send")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("이메일은 필수입니다."));
    }
}
