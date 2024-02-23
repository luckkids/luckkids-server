package com.luckkids.docs.home;

import com.luckkids.api.controller.home.HomeController;
import com.luckkids.api.service.missionOutcome.MissionOutcomeReadService;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeForCalendarResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.missionOutcome.projection.MissionOutcomeCalenderDetailDto;
import com.luckkids.domain.missionOutcome.projection.MissionOutcomeCalenderDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.List;

import static com.luckkids.domain.misson.MissionType.HEALTH;
import static com.luckkids.domain.misson.MissionType.SELF_DEVELOPMENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HomeControllerDocsTest extends RestDocsSupport {

    private final MissionOutcomeReadService missionOutcomeReadService = mock(MissionOutcomeReadService.class);

    @Override
    protected Object initController() {
        return new HomeController(missionOutcomeReadService);
    }

    @DisplayName("홈 화면의 캘린더 정보를 조회하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void getMissionOutcomeForCalendar() throws Exception {
        // given
        given(missionOutcomeReadService.getMissionOutcomeForCalendar(any(LocalDate.class)))
            .willReturn(
                createMissionOutcomeForCalenderResponse(
                    LocalDate.of(2023, 11, 1),
                    LocalDate.of(2023, 12, 31),
                    List.of(
                        createMissionOutcomeCalenderDto(22, true),
                        createMissionOutcomeCalenderDto(23, true),
                        createMissionOutcomeCalenderDto(24, false)
                    )
                )
            );

        // when // then
        mockMvc.perform(
                get("/api/v1/home/calender")
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("home-calender",
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
                    fieldWithPath("data.calender[]").type(JsonFieldType.ARRAY)
                        .description("알림 여부"),
                    fieldWithPath("data.calender[].missionDate").type(JsonFieldType.STRING)
                        .description("미션 날짜"),
                    fieldWithPath("data.calender[].hasSucceed").type(JsonFieldType.BOOLEAN)
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
                new MissionOutcomeCalenderDetailDto(HEALTH, "운동하기"),
                new MissionOutcomeCalenderDetailDto(SELF_DEVELOPMENT, "책 읽기")
            ));

        // when // then
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/home/calender/{missionDate}", LocalDate.of(2023, 12, 26))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("home-calender-detail",
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

    private MissionOutcomeForCalendarResponse createMissionOutcomeForCalenderResponse(
        LocalDate statDate,
        LocalDate endDate,
        List<MissionOutcomeCalenderDto> data
    ) {
        return MissionOutcomeForCalendarResponse.builder()
            .startDate(statDate)
            .endDate(endDate)
            .calender(data).build();
    }

    private MissionOutcomeCalenderDto createMissionOutcomeCalenderDto(int dayOfMonth, boolean hasSucceed) {
        return new MissionOutcomeCalenderDto(LocalDate.of(2023, 11, dayOfMonth), hasSucceed);
    }

}
