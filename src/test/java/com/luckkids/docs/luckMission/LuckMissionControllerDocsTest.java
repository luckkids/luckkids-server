package com.luckkids.docs.luckMission;

import com.luckkids.api.controller.luckMission.LuckMissionController;
import com.luckkids.api.service.luckMission.LuckMissionReadService;
import com.luckkids.api.service.luckMission.response.LuckMissionResponse;
import com.luckkids.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LuckMissionControllerDocsTest extends RestDocsSupport {

    private final LuckMissionReadService luckMissionReadService =  mock(LuckMissionReadService.class);

    @Override
    protected Object initController() {
        return new LuckMissionController(luckMissionReadService);
    }

    @DisplayName("이메일 중복체크 API")
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
                get("/api/v1/luckMission/")
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("luckMission-get",
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

    private LuckMissionResponse createMissionResponse(String description, LocalTime alertTime){
        return LuckMissionResponse.builder()
            .description(description)
            .alertTime(alertTime)
            .build();
    }
}
