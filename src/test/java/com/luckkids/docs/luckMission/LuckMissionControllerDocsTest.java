package com.luckkids.docs.luckMission;

import com.luckkids.api.controller.luckkidsMission.LuckkidsMissionController;
import com.luckkids.api.controller.luckkidsMission.request.LuckkidsMissionListRequest;
import com.luckkids.api.controller.luckkidsMission.request.LuckkidsMissionRequest;
import com.luckkids.api.controller.version.VersionController;
import com.luckkids.api.controller.version.request.VersionSaveRequest;
import com.luckkids.api.service.luckkidsMission.LuckkidsMissionService;
import com.luckkids.api.service.luckkidsMission.request.LuckkidsMissionListServiceRequest;
import com.luckkids.api.service.luckkidsMission.request.LuckkidsMissionServiceRequest;
import com.luckkids.api.service.luckkidsMission.response.LuckkidsMissionListSaveResponse;
import com.luckkids.api.service.luckkidsMission.response.LuckkidsMissionResponse;
import com.luckkids.api.service.luckkidsMission.response.LuckkidsMissionSaveResponse;
import com.luckkids.api.service.version.VersionReadService;
import com.luckkids.api.service.version.VersionService;
import com.luckkids.api.service.version.request.VersionSaveServiceRequest;
import com.luckkids.api.service.version.response.VersionResponse;
import com.luckkids.api.service.version.response.VersionSaveResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.misson.MissionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

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

public class LuckMissionControllerDocsTest extends RestDocsSupport {

    private final LuckkidsMissionService luckkidsMissionService = mock(LuckkidsMissionService.class);

    @Override
    protected Object initController() {
        return new LuckkidsMissionController(luckkidsMissionService);
    }

    @DisplayName("럭키즈 미션을 등록하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void saveVersion() throws Exception {
        // given
        LuckkidsMissionListRequest request = LuckkidsMissionListRequest.builder()
                .missions(
                        List.of(
                                LuckkidsMissionRequest.builder()
                                        .missionType(MissionType.HEALTH)
                                        .missionDescription("아침 일찍 일어나기")
                                        .alertTime(LocalTime.of(1, 0))
                                        .sort(1)
                                        .build(),
                                LuckkidsMissionRequest.builder()
                                        .missionType(MissionType.HOUSEKEEPING)
                                        .missionDescription("아침에 청소기 돌리기")
                                        .alertTime(LocalTime.of(1, 0))
                                        .sort(1)
                                        .build()
                        )
                )
                .build();

        given(luckkidsMissionService.createLuckkidsMission(any(LuckkidsMissionListServiceRequest.class)))
                .willReturn(
                        LuckkidsMissionListSaveResponse.builder()
                                .missions(
                                        List.of(
                                                LuckkidsMissionSaveResponse.builder()
                                                        .id(1)
                                                        .missionType(MissionType.HEALTH)
                                                        .missionDescription("아침 일찍 일어나기")
                                                        .alertTime(LocalTime.of(1, 0))
                                                        .sort(1)
                                                        .build(),
                                                LuckkidsMissionSaveResponse.builder()
                                                        .id(1)
                                                        .missionType(MissionType.HOUSEKEEPING)
                                                        .missionDescription("아침에 청소기 돌리기")
                                                        .alertTime(LocalTime.of(1, 0))
                                                        .sort(1)
                                                        .build()
                                        )
                                )
                                .build()
                );

        // when // then
        mockMvc.perform(
                        post("/api/v1/luckkidsMission")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("luckkidsMission-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("missions[]").type(JsonFieldType.ARRAY)
                                        .description("미션 목록"),
                                fieldWithPath("missions[].missionType").type(JsonFieldType.STRING)
                                        .description("미션 종류. 가능한 값: " + Arrays.toString(MissionType.values())),
                                fieldWithPath("missions[].missionDescription").type(JsonFieldType.STRING)
                                        .description("미션명"),
                                fieldWithPath("missions[].alertTime").type(JsonFieldType.STRING)
                                        .description("알림시간"),
                                fieldWithPath("missions[].sort").type(JsonFieldType.NUMBER)
                                        .description("정렬값")
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
                                fieldWithPath("data.missions[]").type(JsonFieldType.ARRAY)
                                        .description("미션 목록"),
                                fieldWithPath("data.missions[].id").type(JsonFieldType.NUMBER)
                                        .description("ID"),
                                fieldWithPath("data.missions[].missionType").type(JsonFieldType.STRING)
                                        .description("미션 종류. 가능한 값: " + Arrays.toString(MissionType.values())),
                                fieldWithPath("data.missions[].missionDescription").type(JsonFieldType.STRING)
                                        .description("미션명"),
                                fieldWithPath("data.missions[].alertTime").type(JsonFieldType.STRING)
                                        .description("알림시간"),
                                fieldWithPath("data.missions[].sort").type(JsonFieldType.NUMBER)
                                        .description("정렬값")
                        )
                ));
    }
}
