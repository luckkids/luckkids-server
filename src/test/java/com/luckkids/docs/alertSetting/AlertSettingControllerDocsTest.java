package com.luckkids.docs.alertSetting;

import com.luckkids.api.controller.alertSetting.AlertSettingController;
import com.luckkids.api.controller.alertSetting.request.AlertSettingRequest;
import com.luckkids.api.controller.alertSetting.request.AlertSettingUpdateRequest;
import com.luckkids.api.service.alertSetting.AlertSettingReadService;
import com.luckkids.api.service.alertSetting.AlertSettingService;
import com.luckkids.api.service.alertSetting.request.AlertSettingServiceRequest;
import com.luckkids.api.service.alertSetting.request.AlertSettingUpdateServiceRequest;
import com.luckkids.api.service.alertSetting.response.AlertSettingResponse;
import com.luckkids.api.service.alertSetting.response.AlertSettingUpdateResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.misson.AlertStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AlertSettingControllerDocsTest extends RestDocsSupport {

    private final AlertSettingReadService alertSettingReadService = mock(AlertSettingReadService.class);
    private final AlertSettingService alertSettingService = mock(AlertSettingService.class);

    @Override
    protected Object initController() {
        return new AlertSettingController(alertSettingReadService, alertSettingService);
    }

    @DisplayName("알림설정조회 API")
    @Test
    @WithMockUser(roles = "USER")
    void getAlertSetting() throws Exception {
        // given
        AlertSettingRequest request = AlertSettingRequest.builder()
            .deviceId("testdeviceId")
            .build();

        given(alertSettingReadService.getAlertSetting(any(AlertSettingServiceRequest.class)))
            .willReturn(
                AlertSettingResponse.builder()
                    .luck(AlertStatus.CHECKED)
                    .mission(AlertStatus.CHECKED)
                    .notice(AlertStatus.CHECKED)
                    .entire(AlertStatus.CHECKED)
                    .build()
            );

        // when // then
        mockMvc.perform(
                get("/api/v1/alertSetting")
                    .param("deviceId", request.getDeviceId())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-alertSetting",
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
                    fieldWithPath("data.entire").type(JsonFieldType.STRING)
                        .description("전체알람설정"),
                    fieldWithPath("data.mission").type(JsonFieldType.STRING)
                        .description("미션알림설정"),
                    fieldWithPath("data.luck").type(JsonFieldType.STRING)
                        .description("7시행운문구알림설정"),
                    fieldWithPath("data.notice").type(JsonFieldType.STRING)
                        .description("공지사항 알림설정")
                )
            ));
    }

    @DisplayName("알림 수정 API")
    @Test
    @WithMockUser(roles = "USER")
    void updateAlertSetting() throws Exception {
        AlertSettingUpdateRequest request = AlertSettingUpdateRequest.builder()
            .alertType(AlertType.ENTIRE)
            .alertStatus(AlertStatus.UNCHECKED)
            .deviceId("testDeviceId")
            .build();
        // given
        given(alertSettingService.updateAlertSetting(any(AlertSettingUpdateServiceRequest.class)))
            .willReturn(
                AlertSettingUpdateResponse.builder()
                    .luck(AlertStatus.CHECKED)
                    .mission(AlertStatus.CHECKED)
                    .notice(AlertStatus.CHECKED)
                    .entire(AlertStatus.CHECKED)
                    .build()
            );

        // when // then
        mockMvc.perform(
                patch("/api/v1/alertSetting/update")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("update-alertSetting",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("alertType").type(JsonFieldType.STRING)
                        .description("알림타입 가능한 값: " + Arrays.toString(AlertType.values())),
                    fieldWithPath("alertStatus").type(JsonFieldType.STRING)
                        .description("알림여부"),
                    fieldWithPath("deviceId").type(JsonFieldType.STRING)
                        .description("디바이스ID")
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
                    fieldWithPath("data.entire").type(JsonFieldType.STRING)
                        .description("전체알람설정: " + Arrays.toString(AlertStatus.values())),
                    fieldWithPath("data.mission").type(JsonFieldType.STRING)
                        .description("미션알림설정: " + Arrays.toString(AlertStatus.values())),
                    fieldWithPath("data.luck").type(JsonFieldType.STRING)
                        .description("7시행운문구알림설정: " + Arrays.toString(AlertStatus.values())),
                    fieldWithPath("data.notice").type(JsonFieldType.STRING)
                        .description("공지사항 알림설정: " + Arrays.toString(AlertStatus.values()))
                )
            ));
    }
}
