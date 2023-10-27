package com.luckkids.docs.mission;

import com.luckkids.api.controller.mission.MissionController;
import com.luckkids.api.controller.mission.request.MissionCreateRequest;
import com.luckkids.api.controller.mission.request.MissionUpdateRequest;
import com.luckkids.api.service.mission.MissionReadService;
import com.luckkids.api.service.mission.MissionService;
import com.luckkids.api.service.mission.request.MissionCreateServiceRequest;
import com.luckkids.api.service.mission.request.MissionUpdateServiceRequest;
import com.luckkids.api.service.mission.response.MissionResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.jwt.dto.UserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static com.luckkids.domain.misson.AlertStatus.CHECKED;
import static com.luckkids.domain.misson.AlertStatus.UNCHECKED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MissionControllerDocsTest extends RestDocsSupport {

    private final MissionService missionService = mock(MissionService.class);
    private final MissionReadService missionReadService = mock(MissionReadService.class);
    private final SecurityService securityService = mock(SecurityService.class);

    @Override
    protected Object initController() {
        return new MissionController(missionService, missionReadService, securityService);
    }

    @DisplayName("신규 미션을 등록하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void createMission() throws Exception {
        // given
        MissionCreateRequest request = MissionCreateRequest.builder()
            .missionDescription("운동하기")
            .alertStatus(CHECKED)
            .alertTime(LocalTime.of(18, 30))
            .build();

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo());

        given(missionService.createMission(any(MissionCreateServiceRequest.class), anyInt()))
            .willReturn(
                createMissionResponse(1, "운동하기", CHECKED, LocalTime.of(18, 0))
            );

        // when // then
        mockMvc.perform(
                post("/api/v1/missions/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("mission-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
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
                        .description("응답 데이터"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                        .description("미션 ID"),
                    fieldWithPath("data.missionDescription").type(JsonFieldType.STRING)
                        .description("미션 내용"),
                    fieldWithPath("data.alertStatus").type(JsonFieldType.STRING)
                        .description("알림 여부"),
                    fieldWithPath("data.alertTime").type(JsonFieldType.STRING)
                        .description("알림 시간")
                )
            ));
    }

    @DisplayName("등록된 미션을 수정하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void updateMission() throws Exception {
        // given
        MissionUpdateRequest request = MissionUpdateRequest.builder()
            .missionDescription("운동하기")
            .alertStatus(CHECKED)
            .alertTime(LocalTime.of(18, 30))
            .build();

        given(missionService.updateMission(anyInt(), any(MissionUpdateServiceRequest.class)))
            .willReturn(
                createMissionResponse(1, "운동하기", CHECKED, LocalTime.of(18, 0))
            );

        // when // then
        mockMvc.perform(
                patch("/api/v1/missions/{missionId}", 1)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("mission-update",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
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
                        .description("응답 데이터"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                        .description("미션 ID"),
                    fieldWithPath("data.missionDescription").type(JsonFieldType.STRING)
                        .description("미션 내용"),
                    fieldWithPath("data.alertStatus").type(JsonFieldType.STRING)
                        .description("알림 여부"),
                    fieldWithPath("data.alertTime").type(JsonFieldType.STRING)
                        .description("알림 시간")
                )
            ));
    }

    @DisplayName("등록된 미션을 조회하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void getMission() throws Exception {
        // given
        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo());

        given(missionReadService.getMission(anyInt()))
            .willReturn(
                List.of(
                    createMissionResponse(1, "운동하기", CHECKED, LocalTime.of(18, 0)),
                    createMissionResponse(2, "책 읽기", UNCHECKED, LocalTime.of(20, 0))
                )
            );

        // when // then
        mockMvc.perform(
                get("/api/v1/missions")
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("mission-get",
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
                        .description("미션 ID"),
                    fieldWithPath("data[].missionDescription").type(JsonFieldType.STRING)
                        .description("미션 내용"),
                    fieldWithPath("data[].alertStatus").type(JsonFieldType.STRING)
                        .description("알림 여부"),
                    fieldWithPath("data[].alertTime").type(JsonFieldType.STRING)
                        .description("알림 시간")
                )
            ));
    }

    private MissionResponse createMissionResponse(int id, String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        return MissionResponse.builder()
            .id(id)
            .missionDescription(missionDescription)
            .alertStatus(alertStatus)
            .alertTime(alertTime)
            .build();
    }

    private UserInfo createUserInfo() {
        return UserInfo.builder()
            .userId(1)
            .email("")
            .build();
    }
}
