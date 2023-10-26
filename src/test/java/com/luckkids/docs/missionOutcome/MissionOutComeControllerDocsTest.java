package com.luckkids.docs.missionOutcome;

import com.luckkids.api.controller.missionOutcome.MissionOutcomeController;
import com.luckkids.api.controller.missionOutcome.request.MissionOutcomeUpdateRequest;
import com.luckkids.api.service.missionOutcome.MissionOutcomeService;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.missionOutcome.MissionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;

import static com.luckkids.domain.missionOutcome.MissionStatus.SUCCEED;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MissionOutComeControllerDocsTest extends RestDocsSupport {

    private final MissionOutcomeService missionOutcomeService = mock(MissionOutcomeService.class);

    @Override
    protected Object initController() {
        return new MissionOutcomeController(missionOutcomeService);
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
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("missionOutcome-update",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
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
}
