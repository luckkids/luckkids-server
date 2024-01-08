package com.luckkids.api.controller.confirmEmail;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.confirmEmail.request.ConfirmEmailCheckRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ConfirmEmailControllerTest extends ControllerTestSupport {

    @DisplayName("이메일 인증여부를 체크한다.")
    @Test
    @WithMockUser("USER")
    void checkEmail() throws Exception {
        // given
        ConfirmEmailCheckRequest request = ConfirmEmailCheckRequest.builder()
            .email("test@test.com")
            .authKey("testtesttest")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/confirmEmail/check")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("이메일 인증을 할시 이메일은 필수이다.")
    @Test
    @WithMockUser("USER")
    void checkEmailWithoutEmail() throws Exception {
        // given
        ConfirmEmailCheckRequest request = ConfirmEmailCheckRequest.builder()
            .authKey("testtesttest")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/confirmEmail/check")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("이메일은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("이메일 인증을 할시 인증키는 필수이다.")
    @Test
    @WithMockUser("USER")
    void checkEmailWithoutAuthKey() throws Exception {
        // given
        ConfirmEmailCheckRequest request = ConfirmEmailCheckRequest.builder()
            .email("test@test.com")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/confirmEmail/check")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("인증키는 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }
}
