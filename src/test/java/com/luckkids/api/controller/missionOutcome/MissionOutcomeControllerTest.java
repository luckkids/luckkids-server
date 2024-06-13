package com.luckkids.api.controller.missionOutcome;

import static com.luckkids.domain.missionOutcome.MissionStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.missionOutcome.request.MissionOutcomeUpdateRequest;

class MissionOutcomeControllerTest extends ControllerTestSupport {

	@DisplayName("등록되어있는 미션결과를 수정한다.")
	@Test
	@WithMockUser(roles = "USER")
	void updateMissionOutcome() throws Exception {
		// given
		MissionOutcomeUpdateRequest request = MissionOutcomeUpdateRequest.builder()
			.missionStatus(SUCCEED)
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/missionOutcomes/{missionOutcomeId}", 1L)
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

	@DisplayName("등록되어있는 미션결과를 수정할 때 미션 성공 여부는 필수다.")
	@Test
	@WithMockUser(roles = "USER")
	void updateMissionOutcomeWithoutMissionStatus() throws Exception {
		// given
		MissionOutcomeUpdateRequest request = MissionOutcomeUpdateRequest.builder()
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/missionOutcomes/{missionOutcomeId}", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("미션 성공 여부는 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("미션상태를 받아 미션결과를 조회한다.")
	@Test
	@WithMockUser(roles = "USER")
	void getMissionDetailListForStatus() throws Exception {
		// given

		// when // then
		mockMvc.perform(
				get("/api/v1/missionOutcomes")
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("미션상태를 받아 미션결과를 조회한다.")
	@Test
	@WithMockUser(roles = "USER")
	void getMissionOutcomesCount() throws Exception {
		// given

		// when // then
		mockMvc.perform(
				get("/api/v1/missionOutcomes/count")
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}
}