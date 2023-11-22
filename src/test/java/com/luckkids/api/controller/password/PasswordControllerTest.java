package com.luckkids.api.controller.password;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.password.request.UserFindSnsTypeRequest;
import com.luckkids.api.controller.password.request.UserUpdatePasswordRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PasswordControllerTest extends ControllerTestSupport {

    @DisplayName("비밀번호를 재설정한다.")
    @Test
    @WithMockUser(roles = "USER")
    void changePassword() throws Exception {
        // given
        UserUpdatePasswordRequest userUpdatePasswordRequest = UserUpdatePasswordRequest.builder()
            .email("test@email.com")
            .password("1234")
            .build();

        // when // then
        mockMvc.perform(
                patch("/api/v1/password/")
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
    void changePasswordWithoutEmail() throws Exception {
        // given
        UserUpdatePasswordRequest userUpdatePasswordRequest = UserUpdatePasswordRequest.builder()
            .password("1234")
            .build();

        // when // then
        mockMvc.perform(
                patch("/api/v1/password/")
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
    void changePasswordWithoutPassword() throws Exception {
        // given
        UserUpdatePasswordRequest userUpdatePasswordRequest = UserUpdatePasswordRequest.builder()
            .email("test@email.com")
            .build();

        // when // then
        mockMvc.perform(
                patch("/api/v1/password/")
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

    @DisplayName("비밀번호 재설정전 이메일의 회원가입 형태를 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void findSnsType() throws Exception {
        // given
        UserFindSnsTypeRequest userFindSnsTypeRequest = UserFindSnsTypeRequest.builder()
            .email("test@email.com")
            .build();

        // when // then
        mockMvc.perform(
                get("/api/v1/password/findSnsType")
                    .content(objectMapper.writeValueAsString(userFindSnsTypeRequest))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("비밀번호 재설정전 이메일의 회원가입 형태를 조회시 이메일은 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void findSnsTypeWithoutEmail() throws Exception {
        // given
        UserFindSnsTypeRequest userFindSnsTypeRequest = UserFindSnsTypeRequest.builder()
            .build();

        // when // then
        mockMvc.perform(
                get("/api/v1/password/findSnsType")
                    .content(objectMapper.writeValueAsString(userFindSnsTypeRequest))
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
}
