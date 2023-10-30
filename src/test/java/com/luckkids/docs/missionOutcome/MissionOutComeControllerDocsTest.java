package com.luckkids.docs.missionOutcome;

import com.luckkids.api.controller.missionOutcome.MissionOutcomeController;
import com.luckkids.api.controller.missionOutcome.request.MissionOutcomeUpdateRequest;
import com.luckkids.api.service.missionOutcome.MissionOutcomeReadService;
import com.luckkids.api.service.missionOutcome.MissionOutcomeService;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.missionOutcome.MissionStatus;
import com.luckkids.jwt.dto.UserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static com.luckkids.domain.missionOutcome.MissionStatus.FAILED;
import static com.luckkids.domain.missionOutcome.MissionStatus.SUCCEED;
import static java.time.LocalDate.now;
import static java.util.Optional.empty;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MissionOutComeControllerDocsTest extends RestDocsSupport {

    private final MissionOutcomeService missionOutcomeService = mock(MissionOutcomeService.class);
    private final MissionOutcomeReadService missionOutcomeReadService = mock(MissionOutcomeReadService.class);
    private final SecurityService securityService = mock(SecurityService.class);

    @Override
    protected Object initController() {
        return new MissionOutcomeController(missionOutcomeService, missionOutcomeReadService, securityService);
    }

    @DisplayName("등록되어있는 미션결과를 수정하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void updateMissionOutcome() throws Exception {
        // given
        MissionOutcomeUpdateRequest request = MissionOutcomeUpdateRequest.builder()
            .missionStatus(SUCCEED)
            .build();

        given(missionOutcomeService.updateMissionOutcome(1L, request.getMissionStatus()))
            .willReturn(1L);

        // when // then
        mockMvc.perform(
                patch("/api/v1/missionOutComes/{missionOutcomeId}", 1L)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("missionOutcome-update",
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
                    fieldWithPath("data").type(JsonFieldType.NUMBER)
                        .description("미션결과 id")
                )
            ));
    }

    @DisplayName("미션상태를 받아 미션결과를 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionDetailListForStatus() throws Exception {
        // given
        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo());

        given(missionOutcomeReadService.getMissionDetailListForStatus(empty(), 1, now()))
            .willReturn(
                List.of(
                    createMissionOutcomeResponse(1L, "운동하기", LocalTime.of(19, 0), SUCCEED),
                    createMissionOutcomeResponse(2L, "책읽기", LocalTime.of(20, 0), FAILED)
                )
            );

        // when // then
        mockMvc.perform(
                get("/api/v1/missionOutComes")
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
                        fieldWithPath("data[].missionDescription").type(JsonFieldType.STRING)
                            .description("미션결과 내용"),
                        fieldWithPath("data[].alertTime").type(JsonFieldType.STRING)
                            .description("알림 시간"),
                        fieldWithPath("data[].missionStatus").type(JsonFieldType.STRING)
                            .description("미션 성공 여부")
                    )
                )
            );
    }

    private MissionOutcomeResponse createMissionOutcomeResponse(Long id, String missionDescription, LocalTime alertTime, MissionStatus missionStatus) {
        return MissionOutcomeResponse.builder()
            .id(id)
            .missionDescription(missionDescription)
            .alertTime(alertTime)
            .missionStatus(missionStatus)
            .build();
    }

    private UserInfo createUserInfo() {
        return UserInfo.builder()
            .userId(1)
            .email("")
            .build();
    }
}
