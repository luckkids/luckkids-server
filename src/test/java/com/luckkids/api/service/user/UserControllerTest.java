package com.luckkids.api.service.user;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.user.request.UserLuckPhrasesRequest;
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
                patch("/api/v1/user/update")
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
                patch("/api/v1/user/update")
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
}
