package com.luckkids.docs.luckMessageHistory;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import com.luckkids.api.controller.luckMessageHistory.LuckMessageHistoryController;
import com.luckkids.api.controller.luckMessageHistory.request.LuckMessageHistoryRequest;
import com.luckkids.api.service.luckMessageHistory.LuckMessageHistoryReadService;
import com.luckkids.api.service.luckMessageHistory.request.LuckMessageHistoryServiceRequest;
import com.luckkids.api.service.luckMessageHistory.response.LuckMessageHistoryResponse;
import com.luckkids.docs.RestDocsSupport;

public class LuckMessageHistoryDocsTest extends RestDocsSupport {

	private final LuckMessageHistoryReadService luckMessageHistoryReadService = mock(
		LuckMessageHistoryReadService.class);

	@Override
	protected Object initController() {
		return new LuckMessageHistoryController(luckMessageHistoryReadService);
	}

	@DisplayName("오늘의 행운의 한마디 조회 API")
	@Test
	void findOne() throws Exception {
		// given
		LuckMessageHistoryRequest request = LuckMessageHistoryRequest.builder()
			.deviceId("testdeviceId")
			.build();

		given(luckMessageHistoryReadService.findOne(any(LuckMessageHistoryServiceRequest.class)))
			.willReturn(LuckMessageHistoryResponse.builder()
				.messageDescription("테스트 오늘의 한마디")
				.build()
			);

		// when // then
		mockMvc.perform(
				get("/api/v1/luckMessageHistory")
					.param("deviceId", request.getDeviceId())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("luckMessageHistory-findOne",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				queryParameters(
					parameterWithName("deviceId")
						.description("디바이스 ID")
				),
				responseFields(
					fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
						.description("코드"),
					fieldWithPath("httpStatus").type(JsonFieldType.STRING)
						.description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("메세지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("응답 데이터"),
					fieldWithPath("data.messageDescription").type(JsonFieldType.STRING)
						.description("오늘의 행운 한마디")
				)
			));
	}
}
