package com.luckkids.docs.initialSetting;

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
import com.luckkids.api.service.luckMission.LuckMissionReadService;
import com.luckkids.api.service.luckMission.response.LuckMissionResponse;
import com.luckkids.api.service.luckkidsCharacter.LuckkidsCharacterReadService;
import com.luckkids.api.service.luckkidsCharacter.response.LuckCharacterRandResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.misson.AlertStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.luckkids.domain.misson.AlertStatus.CHECKED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InitialSettingControllerDocsTest extends RestDocsSupport {
    private final InitialSettingService initialSettingService = mock(InitialSettingService.class);
    private final LuckkidsCharacterReadService luckkidsCharacterReadService  = mock(LuckkidsCharacterReadService.class);
    private final LuckMissionReadService luckMissionReadService = mock(LuckMissionReadService.class);

    @Override
    protected Object initController() {
        return new InitialSettingController(initialSettingService, luckkidsCharacterReadService, luckMissionReadService);
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

        IntStream.rangeClosed(1, 2).forEach(i -> {
            initialSettingMissionServiceRequests.add(
                InitialSettingMissionServiceRequest.builder()
                    .missionDescription(i + "시에 운동하기")
                    .alertStatus(CHECKED)
                    .alertTime(LocalTime.of(0, 0))
                    .build()
            );
        });

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
            .lottieFile("test.json")
            .nickName("럭키즈!")
            .build();

        List<InitialSettingMissionResponse> initialSettingMissionResponse = Arrays.asList(
            InitialSettingMissionResponse.builder()
                .missionDescription("1시에 운동하기")
                .alertStatus(CHECKED)
                .alertTime(LocalTime.of(0, 0))
                .build(),
            InitialSettingMissionResponse.builder()
                .missionDescription("2시에 운동하기")
                .alertStatus(CHECKED)
                .alertTime(LocalTime.of(0, 0))
                .build()
        );

        InitialSettingAlertResponse initialSettingAlertResponse = InitialSettingAlertResponse.builder()
            .entire(CHECKED)
            .mission(CHECKED)
            .luck(CHECKED)
            .notice(CHECKED)
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
                    fieldWithPath("data.alertSetting.notice").type(JsonFieldType.STRING)
                        .description("공지사항알림. 가능한값: " + Arrays.toString(AlertStatus.values())),
                    fieldWithPath("data.character").type(JsonFieldType.OBJECT)
                        .description("캐릭터설정 응답 데이터"),
                    fieldWithPath("data.character.id").type(JsonFieldType.NUMBER)
                        .description("캐릭터설정 ID"),
                    fieldWithPath("data.character.lottieFile").type(JsonFieldType.STRING)
                        .description("캐릭터 lottie 파일 "),
                    fieldWithPath("data.character.nickName").type(JsonFieldType.STRING)
                        .description("캐릭터 닉네임"),
                    fieldWithPath("data.missions[]").type(JsonFieldType.ARRAY)
                        .description("설정미션 응답 데이터"),
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
    void findAllInitialChracter() throws Exception {
        // given
        given(luckkidsCharacterReadService.findRandomCharacterLevel1())
            .willReturn(LuckCharacterRandResponse.builder()
                .id(1)
                .characterType(CharacterType.SUN)
                .level(1)
                .lottieFile("test.json")
                .imageFile("test.png")
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
                        .description("럭키즈 캐릭터 종류 가능한값: " + Arrays.toString(CharacterType.values())),
                    fieldWithPath("data.level").type(JsonFieldType.NUMBER)
                        .description("럭키즈 캐릭터 레벨"),
                    fieldWithPath("data.lottieFile").type(JsonFieldType.STRING)
                        .description("럭키즈 캐릭터 로티파일"),
                    fieldWithPath("data.imageFile").type(JsonFieldType.STRING)
                        .description("럭키즈 캐릭터 이미지파일")
                )
            ));
    }

    @DisplayName("럭키즈에서 미리 등록한 미션조회 API")
    @Test
    @WithMockUser(roles = "USER")
    void getLuckMission() throws Exception {
        // given
        given(luckMissionReadService.getLuckMissions())
            .willReturn(
                List.of(
                    createMissionResponse("일찍일어나기", LocalTime.of(1, 0)),
                    createMissionResponse("책읽기", LocalTime.of(2, 0))
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
                    fieldWithPath("data[].description").type(JsonFieldType.STRING)
                        .description("미션내용"),
                    fieldWithPath("data[].alertTime").type(JsonFieldType.STRING)
                        .description("알림시간")
                )
            ));
    }

    private LuckMissionResponse createMissionResponse(String description, LocalTime alertTime) {
        return LuckMissionResponse.builder()
            .description(description)
            .alertTime(alertTime)
            .build();
    }
}
