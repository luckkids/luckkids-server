package com.luckkids.api.controller.home;

import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.luckkids.ControllerTestSupport;

class HomeControllerTest extends ControllerTestSupport {

	@DisplayName("홈 화면의 캘린더 정보를 조회한다.")
	@Test
	@WithMockUser(roles = "USER")
	void getMissionOutcomeForCalendar() throws Exception {
		// given

		// when // then
		mockMvc.perform(
				get("/api/v1/home/calendar")
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
				get("/api/v1/home/calendar/{missionDate}", LocalDate.of(2023, 12, 26))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("홈 화면의 메인 정보들을 조회한다.")
	@Test
	@WithMockUser(roles = "USER")
	void getHomeMainInfo() throws Exception {
		// given

		// when // then
		mockMvc.perform(
				get("/api/v1/home/main")
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}
}