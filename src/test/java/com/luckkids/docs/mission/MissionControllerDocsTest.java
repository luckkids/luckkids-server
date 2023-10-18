package com.luckkids.docs.mission;

import com.luckkids.api.controller.mission.MissionController;
import com.luckkids.api.controller.mission.request.MissionCreateRequest;
import com.luckkids.api.service.mission.MissionService;
import com.luckkids.api.service.mission.request.MissionCreateServiceRequest;
import com.luckkids.api.service.mission.response.MissionResponse;
import com.luckkids.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalTime;

import static com.luckkids.domain.misson.AlertStatus.CHECKED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MissionControllerDocsTest extends RestDocsSupport {

    private final MissionService missionService = mock(MissionService.class);

    @Override
    protected Object initController() {
        return new MissionController(missionService);
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

        given(missionService.createMission(any(MissionCreateServiceRequest.class)))
            .willReturn(MissionResponse.builder()
                .id(1)
                .missionDescription("운동하기")
                .alertStatus(CHECKED)
                .alertTime(LocalTime.of(18, 30))
                .build()
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
                        .description("알람 여부"),
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
}
