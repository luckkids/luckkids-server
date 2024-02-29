package com.luckkids.docs.alertHistory;

import com.luckkids.api.controller.alertHistory.AlertHistoryController;
import com.luckkids.api.controller.alertHistory.request.AlertHistoryDeviceIdRequest;
import com.luckkids.api.service.alertHistory.AlertHistoryReadService;
import com.luckkids.api.service.alertHistory.AlertHistoryService;
import com.luckkids.api.service.alertHistory.request.AlertHistoryDeviceIdServiceRequest;
import com.luckkids.api.service.alertHistory.response.AlertHistoryResponse;
import com.luckkids.api.service.alertHistory.response.AlertHistoryStatusResponse;
import com.luckkids.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;

import static com.luckkids.domain.alertHistory.AlertHistoryStatus.CHECKED;
import static com.luckkids.domain.alertHistory.AlertHistoryStatus.UNCHECKED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AlertHistoryControllerDocsTest extends RestDocsSupport {

    private final AlertHistoryService alertHistoryService = mock(AlertHistoryService.class);
    private final AlertHistoryReadService alertHistoryReadService = mock(AlertHistoryReadService.class);

    @Override
    protected Object initController() {
        return new AlertHistoryController(alertHistoryService, alertHistoryReadService);
    }

    @DisplayName("deviceId를 받고 알림 내역을 가져오는 API")
    @Test
    @WithMockUser("USER")
    void getAlertHistory() throws Exception {
        // given
        AlertHistoryDeviceIdRequest request = AlertHistoryDeviceIdRequest.builder()
            .deviceId("testdeviceId")
            .build();

        AlertHistoryResponse response = AlertHistoryResponse.builder()
            .id(1L)
            .alertDescription("알림 내역")
            .alertHistoryStatus(UNCHECKED)
            .createdDate(LocalDateTime.of(2024, 2, 29, 16, 0, 0))
            .build();

        given(alertHistoryReadService.getAlertHistory(any(AlertHistoryDeviceIdServiceRequest.class)))
            .willReturn(response);

        // when // then
        mockMvc.perform(
                get("/api/v1/alertHistories")
                    .param("deviceId", request.getDeviceId())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("alertHistory-get",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("deviceId")
                        .description("디바이스 ID")
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
                        .description("알림 내역 ID"),
                    fieldWithPath("data.alertDescription").type(JsonFieldType.STRING)
                        .description("알림 내역 내용"),
                    fieldWithPath("data.alertHistoryStatus").type(JsonFieldType.STRING)
                        .description("알림 내역 읽음 상태"),
                    fieldWithPath("data.createdDate").type(JsonFieldType.STRING)
                        .description("알림 내역 등록일")
                )
            ));
    }

    @DisplayName("알림 내역 ID를 받아 알림 내역 상태를 업데이트하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void updateAlertHistoryStatus() throws Exception {
        // given
        Long id = 1L;

        AlertHistoryStatusResponse response = AlertHistoryStatusResponse.of(CHECKED);

        given(alertHistoryService.updateAlertHistoryStatus(id))
            .willReturn(response);

        // when // then
        mockMvc.perform(
                patch("/api/v1/alertHistories/{id}", id)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("alertHistory-update",
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("id")
                        .description("알림 내역 ID")
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
                    fieldWithPath("data.alertHistoryStatus").type(JsonFieldType.STRING)
                        .description("알림 내역 읽음 상태")
                )
            ));
    }
}
