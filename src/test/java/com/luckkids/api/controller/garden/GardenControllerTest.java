package com.luckkids.api.controller.garden;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.page.request.PageInfoRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GardenControllerTest extends ControllerTestSupport {

    @DisplayName("친구 리스트 조회한다.")
    @Test
    @WithMockUser("USER")
    void getFriendList() throws Exception {
        // given
        PageInfoRequest request = PageInfoRequest.builder()
                .build();

        // when // then
        mockMvc.perform(
                        get("/api/v1/garden/list")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("유저들의 1-3위까지 리그 정보를 조회한다.")
    @Test
    @WithMockUser("USER")
    void getUserLeagueTop3() throws Exception {
        // given

        // when // then
        mockMvc.perform(
                        get("/api/v1/garden/league")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("친구를 삭제한다.")
    @Test
    @WithMockUser("USER")
    void deleteFriend() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        delete("/api/v1/garden/{friendId}", 1)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }
}
