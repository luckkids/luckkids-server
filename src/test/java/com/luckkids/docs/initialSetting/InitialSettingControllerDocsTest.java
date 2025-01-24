package com.luckkids.docs.initialSetting;

import static com.luckkids.mission.domain.misson.AlertStatus.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import com.luckkids.api.controller.initialSetting.InitialSettingController;
import com.luckkids.api.service.initialSetting.InitialSettingService;
import com.luckkids.api.service.initialSetting.request.InitialSettingAlertServiceRequest;
import com.luckkids.api.service.initialSetting.request.InitialSettingCharacterServiceRequest;
import com.luckkids.api.service.initialSetting.request.InitialSettingMissionServiceRequest;
import com.luckkids.api.service.initialSetting.request.InitialSettingServiceRequest;
import com.luckkids.api.service.initialSetting.response.InitialSettingAlertResponse;
import com.luckkids.api.service.initialSetting.response.InitialSettingCharacterResponse;
import com.luckkids.api.service.initialSetting.response.InitialSettingMissionResponse;
import com.luckkids.api.service.initialSetting.response.InitialSettingResponse;
import com.luckkids.api.service.luckkidsCharacter.LuckkidsCharacterReadService;
import com.luckkids.api.service.luckkidsCharacter.response.LuckkidsCharacterRandResponse;
import com.luckkids.mission.service.response.LuckkidsMissionResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.mission.domain.misson.AlertStatus;
import com.luckkids.mission.domain.misson.MissionType;
import com.luckkids.mission.service.LuckkidsMissionReadService;

public class InitialSettingControllerDocsTest extends RestDocsSupport {
	private final InitialSettingService initialSettingService = mock(InitialSettingService.class);
	private final LuckkidsCharacterReadService luckkidsCharacterReadService = mock(LuckkidsCharacterReadService.class);

	private final LuckkidsMissionReadService luckkidsMissionReadService = mock(LuckkidsMissionReadService.class);

	@Override
	protected Object initController() {
		return new InitialSettingController(initialSettingService, luckkidsCharacterReadService,
			luckkidsMissionReadService);
	}

	@DisplayName("사용자 초기세팅 API")
	@Test
	@WithMockUser(roles = "USER")
	void createInitialSetting() throws Exception {
		// given
		InitialSettingCharacterServiceRequest initialSettingCharacterServiceRequest = InitialSettingCharacterServiceRequest.builder()
			.id(1)
			.nickName("럭키즈!!")
			.build();

		List<InitialSettingMissionServiceRequest> initialSettingMissionServiceRequests = new ArrayList<>();

		IntStream.rangeClosed(1, 2).forEach(i -> initialSettingMissionServiceRequests.add(
			InitialSettingMissionServiceRequest.builder()
				.luckkidsMissionId(i)
				.missionDescription(i + "시에 운동하기")
				.missionType(MissionType.HEALTH)
				.missionDescription(i + "시에 운동하기")
				.alertStatus(CHECKED)
				.alertTime(LocalTime.of(0, 0))
				.build()
		));

		InitialSettingAlertServiceRequest initialSettingAlertServiceRequest = InitialSettingAlertServiceRequest.builder()
			.deviceId("testDeviceId")
			.alertStatus(CHECKED)
			.build();

		InitialSettingServiceRequest request = InitialSettingServiceRequest.builder()
			.character(initialSettingCharacterServiceRequest)
			.missions(initialSettingMissionServiceRequests)
			.alertSetting(initialSettingAlertServiceRequest)
			.build();

		InitialSettingCharacterResponse initialSettingCharacterResponse = InitialSettingCharacterResponse.builder()
			.id(1)
			.nickName("럭키즈!")
			.build();

		List<InitialSettingMissionResponse> initialSettingMissionResponse = Arrays.asList(
			InitialSettingMissionResponse.builder()
				.luckkidsMissionId(1)
				.missionType(MissionType.HEALTH)
				.missionDescription("1시에 운동하기")
				.alertStatus(CHECKED)
				.alertTime(LocalTime.of(0, 0))
				.build(),
			InitialSettingMissionResponse.builder()
				.luckkidsMissionId(2)
				.missionType(MissionType.HEALTH)
				.missionDescription("2시에 운동하기")
				.alertStatus(CHECKED)
				.alertTime(LocalTime.of(0, 0))
				.build()
		);

		InitialSettingAlertResponse initialSettingAlertResponse = InitialSettingAlertResponse.builder()
			.entire(CHECKED)
			.mission(CHECKED)
			.luck(CHECKED)
			.friend(CHECKED)
			.notice(CHECKED)
			.luckMessageAlertTime(LocalTime.of(7, 0))
			.build();

		given(initialSettingService.initialSetting(any(InitialSettingServiceRequest.class)))
			.willReturn(InitialSettingResponse.builder()
				.missions(initialSettingMissionResponse)
				.character(initialSettingCharacterResponse)
				.alertSetting(initialSettingAlertResponse)
				.build()
			);

		// when // then
		mockMvc.perform(
				post("/api/v1/initialSetting")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isCreated())
			.andDo(document("create-initialSetting",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("alertSetting").type(JsonFieldType.OBJECT)
						.description("알람설정 요청 데이터"),
					fieldWithPath("alertSetting.deviceId").type(JsonFieldType.STRING)
						.description("디바이스ID"),
					fieldWithPath("alertSetting.alertStatus").type(JsonFieldType.STRING)
						.description("알림상태. 가능한값: " + Arrays.toString(AlertStatus.values())),
					fieldWithPath("character").type(JsonFieldType.OBJECT)
						.description("캐릭터설정 요청 데이터"),
					fieldWithPath("character.id").type(JsonFieldType.NUMBER)
						.description("럭키즈 캐릭터 ID"),
					fieldWithPath("character.nickName").type(JsonFieldType.STRING)
						.description("캐릭터 닉네임"),
					fieldWithPath("missions[]").type(JsonFieldType.ARRAY)
						.description("설정미션 요청 데이터"),
					fieldWithPath("missions[].luckkidsMissionId").type(JsonFieldType.NUMBER)
						.description("럭키즈 대표 미션 ID (null일 때는 개인적으로 등록한 미션)"),
					fieldWithPath("missions[].missionType").type(JsonFieldType.STRING)
						.description("미션타입. 가능한값: " + Arrays.toString(MissionType.values())),
					fieldWithPath("missions[].missionDescription").type(JsonFieldType.STRING)
						.description("미션내용"),
					fieldWithPath("missions[].alertTime").type(JsonFieldType.STRING)
						.description("알림시간")
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
					fieldWithPath("data.alertSetting").type(JsonFieldType.OBJECT)
						.description("알람설정 응답 데이터"),
					fieldWithPath("data.alertSetting.entire").type(JsonFieldType.STRING)
						.description("전체알림. 가능한값: " + Arrays.toString(AlertStatus.values())),
					fieldWithPath("data.alertSetting.mission").type(JsonFieldType.STRING)
						.description("미션알림. 가능한값: " + Arrays.toString(AlertStatus.values())),
					fieldWithPath("data.alertSetting.luck").type(JsonFieldType.STRING)
						.description("행운알림. 가능한값: " + Arrays.toString(AlertStatus.values())),
					fieldWithPath("data.alertSetting.friend").type(JsonFieldType.STRING)
						.description("친구알림. 가능한값: " + Arrays.toString(AlertStatus.values())),
					fieldWithPath("data.alertSetting.notice").type(JsonFieldType.STRING)
						.description("공지사항알림. 가능한값: " + Arrays.toString(AlertStatus.values())),
					fieldWithPath("data.alertSetting.luckMessageAlertTime").type(JsonFieldType.STRING)
						.description("행운의 한마디 알림시간"),
					fieldWithPath("data.character").type(JsonFieldType.OBJECT)
						.description("캐릭터설정 응답 데이터"),
					fieldWithPath("data.character.id").type(JsonFieldType.NUMBER)
						.description("캐릭터설정 ID"),
					fieldWithPath("data.character.nickName").type(JsonFieldType.STRING)
						.description("캐릭터 닉네임"),
					fieldWithPath("data.missions[]").type(JsonFieldType.ARRAY)
						.description("설정미션 응답 데이터"),
					fieldWithPath("data.missions[].luckkidsMissionId").type(JsonFieldType.NUMBER)
						.description("럭키즈 대표 미션 ID (null일 때는 개인적으로 등록한 미션)"),
					fieldWithPath("data.missions[].missionType").type(JsonFieldType.STRING)
						.description("미션타입. 가능한값: " + Arrays.toString(MissionType.values())),
					fieldWithPath("data.missions[].missionDescription").type(JsonFieldType.STRING)
						.description("미션내용"),
					fieldWithPath("data.missions[].alertStatus").type(JsonFieldType.STRING)
						.description("미션알림여부. 가능한값: " + Arrays.toString(AlertStatus.values())),
					fieldWithPath("data.missions[].alertTime").type(JsonFieldType.STRING)
						.description("미션알림시간")
				)
			));
	}

	@DisplayName("초기캐릭터 랜덤조회 API")
	@Test
	@WithMockUser(roles = "USER")
	void findAllInitialCharacter() throws Exception {
		// given
		given(luckkidsCharacterReadService.findRandomCharacterLevel1())
			.willReturn(LuckkidsCharacterRandResponse.builder()
				.id(1)
				.characterType(CharacterType.SUN)
				.level(1)
				.build());

		// when // then
		mockMvc.perform(
				get("/api/v1/initialSetting/character")
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("get-initialCharacter",
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
					fieldWithPath("data.id").type(JsonFieldType.NUMBER)
						.description("럭키즈 캐릭터 ID"),
					fieldWithPath("data.characterType").type(JsonFieldType.STRING)
						.description("럭키즈 캐릭터 타입, 가능한값: " + Arrays.toString(CharacterType.values())),
					fieldWithPath("data.level").type(JsonFieldType.NUMBER)
						.description("럭키즈 캐릭터 레벨")
				)
			));
	}

	@DisplayName("럭키즈에서 미리 등록한 미션조회 API")
	@Test
	@WithMockUser(roles = "USER")
	void getLuckMission() throws Exception {
		// given
		given(luckkidsMissionReadService.getLuckMissions())
			.willReturn(
				List.of(
					createMissionResponse(1, MissionType.HEALTH, "일찍일어나기", LocalTime.of(1, 0)),
					createMissionResponse(2, MissionType.MINDSET, "책읽기", LocalTime.of(2, 0))
				)
			);

		// when // then
		mockMvc.perform(
				get("/api/v1/initialSetting/luckMission")
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("get-luckMission",
				preprocessResponse(prettyPrint()),
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
						.description("럭키즈 대표 미션 ID (null일 때는 개인적으로 등록한 미션)"),
					fieldWithPath("data[].missionType").type(JsonFieldType.STRING)
						.description("미션타입. 가능한값: " + Arrays.toString(MissionType.values())),
					fieldWithPath("data[].missionDescription").type(JsonFieldType.STRING)
						.description("미션내용"),
					fieldWithPath("data[].alertTime").type(JsonFieldType.STRING)
						.description("알림시간"),
					fieldWithPath("data[].sort").type(JsonFieldType.NUMBER)
						.description("정렬값")
				)
			));
	}

	private LuckkidsMissionResponse createMissionResponse(int luckkidsMissionId, MissionType missionType,
		String missionDescription,
		LocalTime alertTime) {
		return LuckkidsMissionResponse.builder()
			.id(luckkidsMissionId)
			.missionType(missionType)
			.missionDescription(missionDescription)
			.alertTime(alertTime)
			.sort(1)
			.build();
	}
}
