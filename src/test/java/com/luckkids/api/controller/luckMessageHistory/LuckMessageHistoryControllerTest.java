package com.luckkids.api.controller.luckMessageHistory;

import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.luckMessageHistory.request.LuckMessageHistoryRequest;

public class LuckMessageHistoryControllerTest extends ControllerTestSupport {

	@DisplayName("오늘의 행운의 한마디를 조회한다.")
	@Test
	@WithMockUser(roles = "USER")
	void findOneTest() throws Exception {
		// given
		LuckMessageHistoryRequest request = LuckMessageHistoryRequest.builder()
			.deviceId("testDeviceId")
			.build();

		// when // then
		mockMvc.perform(
				get("/api/v1/luckMessageHistory")
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

	@DisplayName("오늘의 행운의 한마디를 조회할 시 디바이스ID는 필수입니다.")
	@Test
	@WithMockUser(roles = "USER")
	void findOneNotExistDeviceIdTest() throws Exception {
		// given
		LuckMessageHistoryRequest request = LuckMessageHistoryRequest.builder()
			.build();

		// when // then
		mockMvc.perform(
				get("/api/v1/luckMessageHistory")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("디바이스ID는 필수입니다."));
	}
}
