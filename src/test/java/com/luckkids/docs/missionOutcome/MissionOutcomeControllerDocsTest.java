package com.luckkids.docs.missionOutcome;

import static com.luckkids.domain.luckkidsCharacter.CharacterType.*;
import static com.luckkids.domain.missionOutcome.MissionStatus.*;
import static com.luckkids.domain.misson.AlertStatus.*;
import static com.luckkids.domain.misson.MissionType.*;
import static java.time.LocalDate.*;
import static java.util.Optional.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import com.luckkids.api.controller.missionOutcome.MissionOutcomeController;
import com.luckkids.api.controller.missionOutcome.request.MissionOutcomeUpdateRequest;
import com.luckkids.api.service.missionOutcome.MissionOutcomeReadService;
import com.luckkids.api.service.missionOutcome.MissionOutcomeService;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeCountResponse;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeResponse;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeUpdateResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.missionOutcome.MissionStatus;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.MissionType;

public class MissionOutcomeControllerDocsTest extends RestDocsSupport {

	private final MissionOutcomeService missionOutcomeService = mock(MissionOutcomeService.class);
	private final MissionOutcomeReadService missionOutcomeReadService = mock(MissionOutcomeReadService.class);

	@Override
	protected Object initController() {
		return new MissionOutcomeController(missionOutcomeService, missionOutcomeReadService);
	}

	@DisplayName("등록되어있는 미션결과를 수정하는 API (레벨업 O)")
	@Test
	@WithMockUser(roles = "USER")
	void updateMissionOutcomeWithLevelUp_O() throws Exception {
		// given
		MissionOutcomeUpdateRequest request = MissionOutcomeUpdateRequest.builder()
			.missionStatus(SUCCEED)
			.build();

		given(missionOutcomeService.updateMissionOutcome(1L, request.getMissionStatus()))
			.willReturn(MissionOutcomeUpdateResponse.of(true, CLOVER, 2));

		// when // then
		mockMvc.perform(
				patch("/api/v1/missionOutcomes/{missionOutcomeId}", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("missionOutcome-update-levelUp_O",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("missionOutcomeId")
						.description("미션결과 ID")
				),
				requestFields(
					fieldWithPath("missionStatus").type(JsonFieldType.STRING)
						.description("미션 성공 여부. 가능한 값: " + Arrays.toString(MissionStatus.values()))
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
					fieldWithPath("data.levelUpResult").type(JsonFieldType.BOOLEAN)
						.description("레벨업 여부"),
					fieldWithPath("data.characterType").type(JsonFieldType.STRING)
						.description("럭키즈 캐릭터 타입, 가능한값: " + Arrays.toString(CharacterType.values())),
					fieldWithPath("data.level").type(JsonFieldType.NUMBER)
						.description("캐릭터 레벨")
				)
			));
	}

	@DisplayName("등록되어있는 미션결과를 수정하는 API (레벨업 X)")
	@Test
	@WithMockUser(roles = "USER")
	void updateMissionOutcomeWithLevelUp_X() throws Exception {
		// given
		MissionOutcomeUpdateRequest request = MissionOutcomeUpdateRequest.builder()
			.missionStatus(SUCCEED)
			.build();

		given(missionOutcomeService.updateMissionOutcome(1L, request.getMissionStatus()))
			.willReturn(
				MissionOutcomeUpdateResponse.of(false, null, 0)
			);

		// when // then
		mockMvc.perform(
				patch("/api/v1/missionOutcomes/{missionOutcomeId}", 1L)
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("missionOutcome-update-levelUp_X",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("missionOutcomeId")
						.description("미션결과 ID")
				),
				requestFields(
					fieldWithPath("missionStatus").type(JsonFieldType.STRING)
						.description("미션 성공 여부. 가능한 값: " + Arrays.toString(MissionStatus.values()))
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
					fieldWithPath("data.levelUpResult").type(JsonFieldType.BOOLEAN)
						.description("레벨업 여부"),
					fieldWithPath("data.characterType").type(JsonFieldType.NULL)
						.description("캐릭터 타입, 가능한값: " + Arrays.toString(CharacterType.values())),
					fieldWithPath("data.level").type(JsonFieldType.NUMBER)
						.description("캐릭터 레벨")
				)
			));
	}

	@DisplayName("미션상태를 받아 미션결과를 조회하는 API")
	@Test
	@WithMockUser(roles = "USER")
	void getMissionDetailListForStatus() throws Exception {
		// given
		given(missionOutcomeReadService.getMissionOutcomeDetailListForStatus(empty(), now()))
			.willReturn(
				List.of(
					createMissionOutcomeResponse(1L, HEALTH, "운동하기", LocalTime.of(19, 0), UNCHECKED, SUCCEED),
					createMissionOutcomeResponse(2L, SELF_DEVELOPMENT, "책읽기", LocalTime.of(20, 0), CHECKED, FAILED)
				)
			);

		// when // then
		mockMvc.perform(
				get("/api/v1/missionOutcomes")
			)
			.andDo(print())
			.andDo(document("missionOutcome-get",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					queryParameters(
						parameterWithName("missionStatus")
							.description("미션 성공 여부. 가능한 값: " + Arrays.toString(MissionStatus.values()))
							.optional()
					),
					responseFields(
						fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
							.description("코드"),
						fieldWithPath("httpStatus").type(JsonFieldType.STRING)
							.description("상태"),
						fieldWithPath("message").type(JsonFieldType.STRING)
							.description("메세지"),
						fieldWithPath("data[]").type(JsonFieldType.ARRAY)
							.description("응답 데이터"),
						fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
							.description("미션결과 ID"),
						fieldWithPath("data[].missionType").type(JsonFieldType.STRING)
							.description("미션결과 미션타입"),
						fieldWithPath("data[].missionDescription").type(JsonFieldType.STRING)
							.description("미션결과 내용"),
						fieldWithPath("data[].alertTime").type(JsonFieldType.STRING)
							.description("알림 시간"),
						fieldWithPath("data[].alertStatus").type(JsonFieldType.STRING)
							.description("알림 상태"),
						fieldWithPath("data[].missionStatus").type(JsonFieldType.STRING)
							.description("미션 성공 여부")
					)
				)
			);
	}

	@DisplayName("미션상태를 받아 미션결과를 조회하는 API")
	@Test
	@WithMockUser(roles = "USER")
	void getMissionOutcomesCount() throws Exception {
		// given
		given(missionOutcomeReadService.getMissionOutcomesCount())
			.willReturn(
				MissionOutcomeCountResponse.of(2L)
			);

		// when // then
		mockMvc.perform(
				get("/api/v1/missionOutcomes/count")
			)
			.andDo(print())
			.andDo(document("missionOutcome-count",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					responseFields(
						fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
							.description("코드"),
						fieldWithPath("httpStatus").type(JsonFieldType.STRING)
							.description("상태"),
						fieldWithPath("message").type(JsonFieldType.STRING)
							.description("메세지"),
						fieldWithPath("data").type(JsonFieldType.OBJECT)
							.description("응답 데이터"),
						fieldWithPath("data.count").type(JsonFieldType.NUMBER)
							.description("누적된 수행 미션")
					)
				)
			);
	}

	private MissionOutcomeResponse createMissionOutcomeResponse(Long id, MissionType missionType,
		String missionDescription, LocalTime alertTime, AlertStatus alertStatus, MissionStatus missionStatus) {
		return MissionOutcomeResponse.builder()
			.id(id)
			.missionType(missionType)
			.missionDescription(missionDescription)
			.alertTime(alertTime)
			.alertStatus(alertStatus)
			.missionStatus(missionStatus)
			.build();
	}
}
