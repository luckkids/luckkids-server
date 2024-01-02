package com.luckkids.api.controller.withdrawReason;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.withdrawReason.request.WithdrawReasonCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WithdrawReasonControllerTest extends ControllerTestSupport {

    @DisplayName("탈퇴사유를 저장한다.")
    @Test
    @WithMockUser(roles = "USER")
    void createWithdrawReasonTest() throws Exception {
        // given
         WithdrawReasonCreateRequest request = WithdrawReasonCreateRequest.builder()
            .reason("잘 사용하지 않는 앱이예요")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/withdraw/reason")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.statusCode").value("201"))
            .andExpect(jsonPath("$.httpStatus").value("CREATED"))
            .andExpect(jsonPath("$.message").value("CREATED"));
    }

    @DisplayName("탈퇴사유를 저장할시 탈퇴사유는 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void createWithdrawReasonWithoutReasonTest() throws Exception {
        // given
        WithdrawReasonCreateRequest request = WithdrawReasonCreateRequest.builder()
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/withdraw/reason")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("탈퇴사유는 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }
}
