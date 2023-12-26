package com.luckkids.api.controller.home;

import com.luckkids.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HomeControllerTest extends ControllerTestSupport {

    @DisplayName("홈 화면의 캘린더 정보를 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionOutcomeForCalendar() throws Exception {
        // given

        // when // then
        mockMvc.perform(
                get("/api/v1/home/calender")
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("홈 화면의 캘린더 정보중 특정 날짜를 선택해 세부정보를 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionOutcomeForCalendarDetail() throws Exception {
        // given

        // when // then
        mockMvc.perform(
                get("/api/v1/home/calender/{missionDate}", LocalDate.of(2023, 12, 26))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }
}