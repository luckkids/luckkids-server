package com.luckkids.docs.home;

import static com.luckkids.domain.luckkidsCharacter.CharacterType.*;
import static com.luckkids.mission.domain.misson.MissionType.*;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import com.luckkids.api.controller.home.HomeController;
import com.luckkids.notification.service.AlertHistoryReadService;
import com.luckkids.mission.service.MissionOutcomeReadService;
import com.luckkids.mission.service.response.MissionOutcomeForCalendarResponse;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.userCharacter.UserCharacterService;
import com.luckkids.api.service.userCharacter.response.UserCharacterSummaryResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.mission.infra.projection.MissionOutcomeCalendarDetailDto;
import com.luckkids.mission.infra.projection.MissionOutcomeCalendarDto;
import com.luckkids.domain.userCharacter.projection.UserCharacterSummaryDto;

public class HomeControllerDocsTest extends RestDocsSupport {

	private final MissionOutcomeReadService missionOutcomeReadService = mock(MissionOutcomeReadService.class);
	private final UserReadService userReadService = mock(UserReadService.class);
	private final UserCharacterService userCharacterService = mock(UserCharacterService.class);
	private final AlertHistoryReadService alertHistoryReadService = mock(AlertHistoryReadService.class);

	@Override
	protected Object initController() {
		return new HomeController(missionOutcomeReadService, userReadService, userCharacterService,
			alertHistoryReadService);
	}

	@DisplayName("홈 화면의 캘린더 정보를 조회하는 API")
	@Test
	@WithMockUser(roles = "USER")
	void getMissionOutcomeForCalendar() throws Exception {
		// given
		given(missionOutcomeReadService.getMissionOutcomeForCalendar(any(LocalDate.class), any(), any()))
			.willReturn(
				createMissionOutcomeForCalendarResponse(
					LocalDate.of(2023, 11, 1),
					LocalDate.of(2023, 12, 31),
					List.of(
						createMissionOutcomeCalendarDto(11, 1, true),
						createMissionOutcomeCalendarDto(11, 2, true),
						createMissionOutcomeCalendarDto(11, 3, false),
						createMissionOutcomeCalendarDto(11, 4, true)
					)
				)
			);

		// when // then
		mockMvc.perform(
				get("/api/v1/home/calendar")
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("home-calendar",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				queryParameters(
					parameterWithName("missionDate")
						.description("미션 검색 날짜. 기본 값: now()")
						.optional()
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
					fieldWithPath("data.startDate").type(JsonFieldType.STRING)
						.description("날짜 범위(시작)"),
					fieldWithPath("data.endDate").type(JsonFieldType.STRING)
						.description("날짜 범위(끝)"),
					fieldWithPath("data.calendar[]").type(JsonFieldType.ARRAY)
						.description("알림 여부"),
					fieldWithPath("data.calendar[].missionDate").type(JsonFieldType.STRING)
						.description("미션 날짜"),
					fieldWithPath("data.calendar[].hasSucceed").type(JsonFieldType.BOOLEAN)
						.description("미션 성공 여부")
				)
			));
	}

	@DisplayName("홈 화면의 캘린더 정보중 특정 날짜를 선택해 세부정보를 조회하는 API")
	@Test
	@WithMockUser(roles = "USER")
	void getMissionOutcomeForCalendarDetail() throws Exception {
		// given
		given(missionOutcomeReadService.getMissionOutcomeForCalendarDetail(any(LocalDate.class)))
			.willReturn(List.of(
				new MissionOutcomeCalendarDetailDto(HEALTH, "운동하기"),
				new MissionOutcomeCalendarDetailDto(SELF_DEVELOPMENT, "책 읽기")
			));

		// when // then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/v1/home/calendar/{missionDate}", LocalDate.of(2023, 12, 26))
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("home-calendar-detail",
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("missionDate")
						.description("미션 날짜")
				),
				responseFields(
					fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
						.description("코드"),
					fieldWithPath("httpStatus").type(JsonFieldType.STRING)
						.description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("메세지"),
					fieldWithPath("data[]").type(JsonFieldType.ARRAY)
						.description("미션 내용들"),
					fieldWithPath("data[].missionType").type(JsonFieldType.STRING)
						.description("미션 타입"),
					fieldWithPath("data[].missionDescription").type(JsonFieldType.STRING)
						.description("미션 내용")
				)
			));
	}

	@DisplayName("홈 화면의 메인 정보들을 조회하는 API")
	@Test
	@WithMockUser(roles = "USER")
	void getHomeMainInfo() throws Exception {
		// given
		given(userReadService.getCharacterAchievementRate()).willReturn(0.75);
		given(userCharacterService.getCharacterSummary()).willReturn(
			UserCharacterSummaryResponse.of(
				new UserCharacterSummaryDto(CLOVER, 1, IN_PROGRESS),
				Map.of(
					CLOVER, 2L,
					CLOUD, 0L,
					STONE, 1L,
					RABBIT, 0L,
					SUN, 5L
				)
			)
		);
		given(missionOutcomeReadService.getMissionOutcomeForCalendar(any(LocalDate.class), any(), any()))
			.willReturn(
				createMissionOutcomeForCalendarResponse(
					LocalDate.of(2024, 2, 24),
					LocalDate.of(2024, 3, 1),
					List.of(
						createMissionOutcomeCalendarDto(2, 24, true),
						createMissionOutcomeCalendarDto(2, 25, true),
						createMissionOutcomeCalendarDto(2, 26, false),
						createMissionOutcomeCalendarDto(2, 27, true),
						createMissionOutcomeCalendarDto(2, 28, true),
						createMissionOutcomeCalendarDto(2, 29, false),
						createMissionOutcomeCalendarDto(3, 1, false)
					)
				)
			);
		given(alertHistoryReadService.hasUncheckedAlerts()).willReturn(true);

		// when // then
		mockMvc.perform(
				get("/api/v1/home/main")
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("home-main",
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
						fieldWithPath("data.luckkidsAchievementRate").type(JsonFieldType.NUMBER)
							.description("럭키즈 달성률"),
						fieldWithPath("data.userCharacterSummaryResponse").type(JsonFieldType.OBJECT)
							.description("캐릭터들 요약 정보"),
						fieldWithPath("data.userCharacterSummaryResponse.inProgressCharacter").type(JsonFieldType.OBJECT)
							.description("진행 중인 캐릭터 정보"),
						fieldWithPath("data.userCharacterSummaryResponse.inProgressCharacter.characterType").type(
								JsonFieldType.STRING)
							.description("캐릭터 타입, 가능한값: " + Arrays.toString(CharacterType.values())),
						fieldWithPath("data.userCharacterSummaryResponse.inProgressCharacter.level").type(
								JsonFieldType.NUMBER)
							.description("캐릭터 레벨"),
						fieldWithPath("data.userCharacterSummaryResponse.inProgressCharacter.characterProgressStatus").type(
								JsonFieldType.STRING)
							.description("캐릭터 진행 상태"),
						fieldWithPath("data.userCharacterSummaryResponse.completedCharacterCount").type(
								JsonFieldType.OBJECT)
							.description("완료한 캐릭터들 수"),
						subsectionWithPath("data.userCharacterSummaryResponse.completedCharacterCount").type(
								JsonFieldType.OBJECT)
							.description("완료한 캐릭터의 수. 키는 캐릭터 타입을 나타내며, 값은 완료된 캐릭터 수."),
						fieldWithPath("data.userCharacterSummaryResponse.completedCharacterCount.RABBIT").type(
								JsonFieldType.NUMBER)
							.description("토끼 캐릭터를 완료한 횟수").optional(),
						fieldWithPath("data.userCharacterSummaryResponse.completedCharacterCount.CLOVER").type(
								JsonFieldType.NUMBER)
							.description("클로버 캐릭터를 완료한 횟수").optional(),
						fieldWithPath("data.userCharacterSummaryResponse.completedCharacterCount.STONE").type(
								JsonFieldType.NUMBER)
							.description("돌 캐릭터를 완료한 횟수").optional(),
						fieldWithPath("data.userCharacterSummaryResponse.completedCharacterCount.CLOUD").type(
								JsonFieldType.NUMBER)
							.description("구름 캐릭터를 완료한 횟수").optional(),
						fieldWithPath("data.userCharacterSummaryResponse.completedCharacterCount.SUN").type(
								JsonFieldType.NUMBER)
							.description("태양 캐릭터를 완료한 횟수").optional(),
						fieldWithPath("data.missionOutcomeForWeekResponse").type(JsonFieldType.OBJECT)
							.description("주간 미션 결과"),
						fieldWithPath("data.missionOutcomeForWeekResponse.startDate").type(JsonFieldType.STRING)
							.description("시작 날짜"),
						fieldWithPath("data.missionOutcomeForWeekResponse.endDate").type(JsonFieldType.STRING)
							.description("종료 날짜"),
						fieldWithPath("data.missionOutcomeForWeekResponse.calendar[].missionDate").type(
								JsonFieldType.STRING)
							.description("미션 날짜"),
						fieldWithPath("data.missionOutcomeForWeekResponse.calendar[].hasSucceed").type(
								JsonFieldType.BOOLEAN)
							.description("미션 성공 여부"),
						fieldWithPath("data.hasUncheckedAlerts").type(JsonFieldType.BOOLEAN)
							.description("읽지 않은 알림이 있는지 여부 (읽지 않은 알림이 있으면 true, 없으면 false)")
					)
				)
			);
	}

	private MissionOutcomeForCalendarResponse createMissionOutcomeForCalendarResponse(
		LocalDate statDate,
		LocalDate endDate,
		List<MissionOutcomeCalendarDto> data
	) {
		return MissionOutcomeForCalendarResponse.builder()
			.startDate(statDate)
			.endDate(endDate)
			.calendar(data).build();
	}

	private MissionOutcomeCalendarDto createMissionOutcomeCalendarDto(int month, int dayOfMonth, boolean hasSucceed) {
		return new MissionOutcomeCalendarDto(LocalDate.of(2024, month, dayOfMonth), hasSucceed);
	}

}
