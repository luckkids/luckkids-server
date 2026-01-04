package com.luckkids.docs.fortuneTestHistory;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import com.luckkids.api.controller.fortunTestHistory.FortuneTestHistoryController;
import com.luckkids.api.controller.fortunTestHistory.request.FortuneTestHistoryCreateRequest;
import com.luckkids.api.service.fortuneTestHistory.FortuneTestHistoryReadService;
import com.luckkids.api.service.fortuneTestHistory.FortuneTestHistoryService;
import com.luckkids.api.service.fortuneTestHistory.request.FortuneTestHistoryCreateServiceRequest;
import com.luckkids.api.service.fortuneTestHistory.response.FortuneTestHistoryResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.fortuneTestHistory.FortuneTestResultType;

public class FortuneTestHistoryControllerDocsTest extends RestDocsSupport {

	private final FortuneTestHistoryService fortuneTestHistoryService = mock(FortuneTestHistoryService.class);
	private final FortuneTestHistoryReadService fortuneTestHistoryReadService = mock(
		FortuneTestHistoryReadService.class);

	@Override
	protected Object initController() {
		return new FortuneTestHistoryController(fortuneTestHistoryService, fortuneTestHistoryReadService);
	}

	@DisplayName("운세 테스트 결과를 저장하는 API")
	@Test
	@WithMockUser(roles = "USER")
	void createFortuneTestHistory() throws Exception {
		// given
		FortuneTestHistoryCreateRequest request = FortuneTestHistoryCreateRequest.builder()
			.nickname("럭키즈")
			.resultType(FortuneTestResultType.A)
			.build();

		given(fortuneTestHistoryService.createFortuneTestHistory(any(FortuneTestHistoryCreateServiceRequest.class)))
			.willReturn(FortuneTestHistoryResponse.builder()
				.id(1)
				.nickname("럭키즈")
				.resultType(FortuneTestResultType.A)
				.build());

		// when // then
		mockMvc.perform(
				post("/api/v1/fortuneTest")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("fortune-test-history-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("nickname").type(JsonFieldType.STRING)
						.description("닉네임"),
					fieldWithPath("resultType").type(JsonFieldType.STRING)
						.description("운세 결과 타입. 가능한 값: " + Arrays.toString(FortuneTestResultType.values()))
				),
				responseFields(
					fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
						.description("코드"),
					fieldWithPath("httpStatus").type(JsonFieldType.STRING)
						.description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("메세지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("응답 데이터")
				)
					.andWithPrefix("data.", fortuneTestHistoryResponseFields())
			));
	}

	@DisplayName("운세 테스트 목록을 조회하는 API")
	@Test
	@WithMockUser(roles = "USER")
	void getFortuneTestHistories() throws Exception {
		// given
		given(fortuneTestHistoryService.findAll())
			.willReturn(List.of(
				FortuneTestHistoryResponse.builder()
					.id(1)
					.nickname("럭키즈")
					.resultType(FortuneTestResultType.A)
					.build(),
				FortuneTestHistoryResponse.builder()
					.id(2)
					.nickname("행운의유저")
					.resultType(FortuneTestResultType.B)
					.build()
			));

		// when // then
		mockMvc.perform(
				get("/api/v1/fortuneTest")
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("fortune-test-history-list",
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
						.description("코드"),
					fieldWithPath("httpStatus").type(JsonFieldType.STRING)
						.description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("메세지"),
					fieldWithPath("data[]").type(JsonFieldType.ARRAY)
						.description("응답 데이터 (리스트)")
				)
					.andWithPrefix("data[].", fortuneTestHistoryResponseFields())
			));
	}

	private FieldDescriptor[] fortuneTestHistoryResponseFields() {
		return new FieldDescriptor[] {
			fieldWithPath("id").type(JsonFieldType.NUMBER)
				.description("운세 테스트 히스토리 ID"),
			fieldWithPath("nickname").type(JsonFieldType.STRING)
				.description("닉네임"),
			fieldWithPath("resultType").type(JsonFieldType.STRING)
				.description("운세 결과 타입")
		};
	}
}
