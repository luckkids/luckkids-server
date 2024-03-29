package com.luckkids.docs.mission;

import com.luckkids.api.controller.mission.MissionController;
import com.luckkids.api.controller.mission.request.MissionCreateRequest;
import com.luckkids.api.controller.mission.request.MissionUpdateRequest;
import com.luckkids.api.service.mission.MissionReadService;
import com.luckkids.api.service.mission.MissionService;
import com.luckkids.api.service.mission.request.MissionCreateServiceRequest;
import com.luckkids.api.service.mission.request.MissionUpdateServiceRequest;
import com.luckkids.api.service.mission.response.MissionDeleteResponse;
import com.luckkids.api.service.mission.response.MissionResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.MissionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.luckkids.domain.misson.AlertStatus.CHECKED;
import static com.luckkids.domain.misson.AlertStatus.UNCHECKED;
import static com.luckkids.domain.misson.MissionType.HEALTH;
import static com.luckkids.domain.misson.MissionType.SELF_DEVELOPMENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MissionControllerDocsTest extends RestDocsSupport {

    private final MissionService missionService = mock(MissionService.class);
    private final MissionReadService missionReadService = mock(MissionReadService.class);

    @Override
    protected Object initController() {
        return new MissionController(missionService, missionReadService);
    }

    @DisplayName("신규 미션을 등록하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void createMission() throws Exception {
        // given
        MissionCreateRequest request = MissionCreateRequest.builder()
            .missionType(HEALTH)
            .missionDescription("운동하기")
            .alertStatus(CHECKED)
            .alertTime(LocalTime.of(18, 30))
            .build();

        given(missionService.createMission(any(MissionCreateServiceRequest.class)))
            .willReturn(
                createMissionResponse(1, HEALTH, "운동하기", CHECKED, LocalTime.of(18, 0))
            );

        // when // then
        mockMvc.perform(
                post("/api/v1/missions/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("mission-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("missionType").type(JsonFieldType.STRING)
                        .description("미션 종류. 가능한 값: " + Arrays.toString(MissionType.values())),
                    fieldWithPath("missionDescription").type(JsonFieldType.STRING)
                        .description("미션 내용"),
                    fieldWithPath("alertStatus").type(JsonFieldType.STRING)
                        .description("알람 여부. 가능한 값: " + Arrays.toString(AlertStatus.values())),
                    fieldWithPath("alertTime").type(JsonFieldType.STRING)
                        .description("알람 시간")
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
                    .andWithPrefix("data.", missionResponseFields())
            ));
    }

    @DisplayName("등록된 미션을 수정하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void updateMission() throws Exception {
        // given
        MissionUpdateRequest request = MissionUpdateRequest.builder()
            .missionType(HEALTH)
            .missionDescription("운동하기")
            .alertStatus(CHECKED)
            .alertTime(LocalTime.of(18, 30))
            .build();

        given(missionService.updateMission(anyInt(), any(MissionUpdateServiceRequest.class)))
            .willReturn(
                createMissionResponse(1, HEALTH, "운동하기", CHECKED, LocalTime.of(18, 0))
            );

        // when // then
        mockMvc.perform(
                patch("/api/v1/missions/{missionId}", 1)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("mission-update",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("missionId")
                        .description("미션 ID")
                ),
                requestFields(
                    fieldWithPath("missionType").type(JsonFieldType.STRING)
                        .description("미션 종류. 가능한 값: " + Arrays.toString(MissionType.values()))
                        .optional(),
                    fieldWithPath("missionDescription").type(JsonFieldType.STRING)
                        .description("미션 내용")
                        .optional(),
                    fieldWithPath("alertStatus").type(JsonFieldType.STRING)
                        .description("알람 여부. 가능한 값: " + Arrays.toString(AlertStatus.values()))
                        .optional(),
                    fieldWithPath("alertTime").type(JsonFieldType.STRING)
                        .description("알람 시간")
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
                        .description("응답 데이터")
                )
                    .andWithPrefix("data.", missionResponseFields())
            ));
    }

    @DisplayName("등록된 미션을 조회하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void getMission() throws Exception {
        // given
        given(missionReadService.getMission())
            .willReturn(
                Map.of(
                    HEALTH, List.of(
                        createMissionResponse(1, HEALTH, "운동하기", CHECKED, LocalTime.of(18, 0)),
                        createMissionResponse(2, HEALTH, "물 마시기", UNCHECKED, LocalTime.of(20, 0))
                    ),
                    SELF_DEVELOPMENT, List.of(
                        createMissionResponse(3, SELF_DEVELOPMENT, "공부하기", CHECKED, LocalTime.of(21, 0)),
                        createMissionResponse(4, SELF_DEVELOPMENT, "책 읽기", UNCHECKED, LocalTime.of(22, 0))
                    )
                )
            );

        // when // then
        mockMvc.perform(
                get("/api/v1/missions")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("mission-get",
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("코드"),
                    fieldWithPath("httpStatus").type(JsonFieldType.STRING).description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.HEALTH[]").type(JsonFieldType.ARRAY).description("건강 관련 미션"),
                    fieldWithPath("data.SELF_DEVELOPMENT[]").type(JsonFieldType.ARRAY).description("자기 계발 관련 미션")
                ).andWithPrefix("data.HEALTH[].", missionResponseFields())
                    .andWithPrefix("data.SELF_DEVELOPMENT[].", missionResponseFields())
            ));
    }

    @DisplayName("등록되어있는 미션을 삭제하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void deleteMission() throws Exception {
        // given
        int missionId = 1;

        given(missionService.deleteMission(anyInt(), any(LocalDateTime.class)))
            .willReturn(MissionDeleteResponse.of(missionId));

        // when // then
        mockMvc.perform(
                delete("/api/v1/missions/{missionId}", missionId)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("mission-delete",
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("missionId")
                        .description("미션 ID")
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
                    fieldWithPath("data.missionId").type(JsonFieldType.NUMBER)
                        .description("미션 ID")
                )
            ));
    }

    private MissionResponse createMissionResponse(int id, MissionType missionType, String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        return MissionResponse.builder()
            .id(id)
            .missionType(missionType)
            .missionDescription(missionDescription)
            .alertStatus(alertStatus)
            .alertTime(alertTime)
            .build();
    }

    private FieldDescriptor[] missionResponseFields() {
        return new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("미션 ID"),
            fieldWithPath("missionType").type(JsonFieldType.STRING).description("미션 타입"),
            fieldWithPath("missionDescription").type(JsonFieldType.STRING).description("미션 내용"),
            fieldWithPath("alertStatus").type(JsonFieldType.STRING).description("알림 여부"),
            fieldWithPath("alertTime").type(JsonFieldType.STRING).description("알림 시간")
        };
    }
}
