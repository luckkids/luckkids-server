package com.luckkids.api.controller.user;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.user.request.UserLuckPhrasesRequest;
import com.luckkids.api.controller.user.request.UserUpdatePasswordRequest;
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
    @DisplayName("사용자 행운문구를 수정한다.")
    @Test
    @WithMockUser(roles = "USER")
    void updateUserLuckPhrases() throws Exception {
        // given
        UserLuckPhrasesRequest request = UserLuckPhrasesRequest.builder()
            .luckPhrases("행운입니다.")
            .build();

        // when // then
        mockMvc.perform(
                patch("/api/v1/user/phrase")
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

    @DisplayName("사용자 행운문구를 수정할시 행운문구는 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void updateUserLuckPhrasesWithoutLuckPhrases() throws Exception {
        // given
        UserLuckPhrasesRequest request = UserLuckPhrasesRequest.builder()
            .build();

        // when // then
        mockMvc.perform(
                patch("/api/v1/user/phrase")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("변경 할 행운문구는 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("비밀번호를 재설정한다.")
    @Test
    @WithMockUser(roles = "USER")
    void updatePassword() throws Exception {
        // given
        UserUpdatePasswordRequest userUpdatePasswordRequest = UserUpdatePasswordRequest.builder()
            .email("test@email.com")
            .password("1234")
            .build();

        // when // then
        mockMvc.perform(
                patch("/api/v1/user/password")
                    .content(objectMapper.writeValueAsString(userUpdatePasswordRequest))
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
    void updatePasswordWithoutEmail() throws Exception {
        // given
        UserUpdatePasswordRequest userUpdatePasswordRequest = UserUpdatePasswordRequest.builder()
            .password("1234")
            .build();

        // when // then
        mockMvc.perform(
                patch("/api/v1/user/password")
                    .content(objectMapper.writeValueAsString(userUpdatePasswordRequest))
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
    void updatePasswordWithoutPassword() throws Exception {
        // given
        UserUpdatePasswordRequest userUpdatePasswordRequest = UserUpdatePasswordRequest.builder()
            .email("test@email.com")
            .build();

        // when // then
        mockMvc.perform(
                patch("/api/v1/user/password")
                    .content(objectMapper.writeValueAsString(userUpdatePasswordRequest))
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
