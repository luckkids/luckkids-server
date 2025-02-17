package com.luckkids.notification.controller.alertHistory;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.page.request.PageInfoRequest;

class AlertHistoryControllerTest extends ControllerTestSupport {

	@DisplayName("알림 내역을 가져온다.")
	@Test
	@WithMockUser("USER")
	void getAlertHistory() throws Exception {
		// given
		PageInfoRequest request = PageInfoRequest.builder()
			.build();

		// when // then
		mockMvc.perform(
				get("/api/v1/alertHistories")
					.param("page", String.valueOf(request.getPage()))
					.param("size", String.valueOf(request.getSize()))
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("알림 내역 ID를 받아 알림 내역 상태를 업데이트한다.")
	@Test
	@WithMockUser(roles = "USER")
	void updateAlertHistoryStatus() throws Exception {
		// given
		Long id = 1L;

		// when // then
		mockMvc.perform(
				patch("/api/v1/alertHistories/{id}", id)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}
}