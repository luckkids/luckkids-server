package com.luckkids.api.controller.user;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.user.request.UserChangePasswordRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends ControllerTestSupport {

    @DisplayName("비밀번호를 재설정한다.")
    @Test
    @WithMockUser(roles = "USER")
    void changePassword() throws Exception {
        // given
        UserChangePasswordRequest userChangePasswordRequest = UserChangePasswordRequest.builder()
            .email("test@email.com")
            .password("1234")
            .build();

        // when // then
        mockMvc.perform(
                patch("/api/v1/user/changePassword")
                    .content(objectMapper.writeValueAsString(userChangePasswordRequest))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("비밀번호를 재설정시 이메일은 필수다.")
    @Test
    @WithMockUser(roles = "USER")
    void changePasswordWithoutEmail() throws Exception {
        // given
        UserChangePasswordRequest userChangePasswordRequest = UserChangePasswordRequest.builder()
            .password("1234")
            .build();

        // when // then
        mockMvc.perform(
                patch("/api/v1/user/changePassword")
                    .content(objectMapper.writeValueAsString(userChangePasswordRequest))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("이메일은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("비밀번호를 재설정시 비밀번호는 필수다.")
    @Test
    @WithMockUser(roles = "USER")
    void changePasswordWithoutPassword() throws Exception {
        // given
        UserChangePasswordRequest userChangePasswordRequest = UserChangePasswordRequest.builder()
            .email("test@email.com")
            .build();

        // when // then
        mockMvc.perform(
                patch("/api/v1/user/changePassword")
                    .content(objectMapper.writeValueAsString(userChangePasswordRequest))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("패스워드는 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }
}
