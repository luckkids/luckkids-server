package com.luckkids.api.controller.fortunTestHistory;

import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.fortunTestHistory.request.FortuneTestHistoryCreateRequest;
import com.luckkids.domain.fortuneTestHistory.FortuneTestResultType;

class FortuneTestHistoryControllerTest extends ControllerTestSupport {

	@DisplayName("운세 테스트 결과를 저장한다.")
	@Test
	@WithMockUser(roles = "USER")
	void createFortuneTestHistory() throws Exception {
		// given
		FortuneTestHistoryCreateRequest request = FortuneTestHistoryCreateRequest.builder()
			.uuid("3a9ff30a-6222-4738-b215-5089d77f475e")
			.nickname("행운이")
			.resultType(FortuneTestResultType.TOKKINGI)
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/fortuneTest")
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

	@DisplayName("운세 테스트 결과를 저장할 때 uuid은 필수다.")
	@Test
	@WithMockUser(roles = "USER")
	void createFortuneTestHistoryWithoutUuid() throws Exception {
		// given
		FortuneTestHistoryCreateRequest request = FortuneTestHistoryCreateRequest.builder()
			.uuid(null)
			.nickname("행운이")
			.resultType(FortuneTestResultType.TOKKINGI)
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/fortuneTest")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("사용자 UUID는 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("운세 테스트 결과를 저장할 때 닉네임은 필수다.")
	@Test
	@WithMockUser(roles = "USER")
	void createFortuneTestHistoryWithoutNickname() throws Exception {
		// given
		FortuneTestHistoryCreateRequest request = FortuneTestHistoryCreateRequest.builder()
			.uuid("3a9ff30a-6222-4738-b215-5089d77f475e")
			.nickname(null)
			.resultType(FortuneTestResultType.TOKKINGI)
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/fortuneTest")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("사용자 닉네임은 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("운세 테스트 결과를 저장할 때 운세 결과는 필수다.")
	@Test
	@WithMockUser(roles = "USER")
	void createFortuneTestHistoryWithoutResultType() throws Exception {
		// given
		FortuneTestHistoryCreateRequest request = FortuneTestHistoryCreateRequest.builder()
			.uuid("3a9ff30a-6222-4738-b215-5089d77f475e")
			.nickname("행운이")
			.resultType(null)
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/fortuneTest")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("운세 결과는 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("저장된 운세 테스트 결과를 조회한다.")
	@Test
	@WithMockUser(roles = "USER")
	void getFortuneTestHistories() throws Exception {
		// given

		// when // then
		mockMvc.perform(
				get("/api/v1/fortuneTest")
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}
}