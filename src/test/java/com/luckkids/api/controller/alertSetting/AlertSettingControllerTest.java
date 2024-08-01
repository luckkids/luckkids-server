package com.luckkids.api.controller.alertSetting;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.alertSetting.request.AlertSettingLuckMessageAlertTimeRequest;
import com.luckkids.api.controller.alertSetting.request.AlertSettingRequest;
import com.luckkids.api.controller.alertSetting.request.AlertSettingUpdateRequest;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.misson.AlertStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AlertSettingControllerTest extends ControllerTestSupport {

    @DisplayName("알림세팅조회")
    @Test
    @WithMockUser("USER")
    void getAlertSetting() throws Exception {
        // given
        AlertSettingRequest request = AlertSettingRequest.builder()
            .deviceId("testdeviceId")
            .build();

        // when // then
        mockMvc.perform(
                get("/api/v1/alertSetting")
                    .param("deviceId", request.getDeviceId())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("알림세팅조회시 디바이스ID는 필수이다.")
    @Test
    @WithMockUser("USER")
    void getAlertSettingWithoutDeviceId() throws Exception {
        // given
        AlertSettingRequest request = AlertSettingRequest.builder()
            .build();

        // when // then
        mockMvc.perform(
                get("/api/v1/alertSetting")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("디바이스ID는 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("알림설정 수정")
    @Test
    @WithMockUser("USER")
    void updateAlertSetting() throws Exception {
        // given
        AlertSettingUpdateRequest request = AlertSettingUpdateRequest.builder()
            .alertType(AlertType.ENTIRE)
            .alertStatus(AlertStatus.CHECKED)
            .deviceId("testdevice")
            .build();

        // when // then
        mockMvc.perform(
                patch("/api/v1/alertSetting/update")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("알림설정 수정시 알림타입은 필수이다.")
    @Test
    @WithMockUser("USER")
    void updateAlertSettingWithoutType() throws Exception {
        // given
        AlertSettingUpdateRequest request = AlertSettingUpdateRequest.builder()
            .alertStatus(AlertStatus.CHECKED)
            .deviceId("testdevice")
            .build();

        // when // then
        mockMvc.perform(
                patch("/api/v1/alertSetting/update")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("알림타입은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("알림설정 수정시 디바이스ID는 필수이다.")
    @Test
    @WithMockUser("USER")
    void updateAlertSettingWithoutDeviceId() throws Exception {
        // given
        AlertSettingUpdateRequest request = AlertSettingUpdateRequest.builder()
            .alertType(AlertType.ENTIRE)
            .alertStatus(AlertStatus.CHECKED)
            .build();

        // when // then
        mockMvc.perform(
                patch("/api/v1/alertSetting/update")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("디바이스ID는 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("알림설정시 알림설정여부는 필수이다.")
    @Test
    @WithMockUser("USER")
    void updateAlertSettingWithoutStatus() throws Exception {
        // given
        AlertSettingUpdateRequest request = AlertSettingUpdateRequest.builder()
            .alertType(AlertType.ENTIRE)
            .deviceId("testDevice")
            .build();

        // when // then
        mockMvc.perform(
                patch("/api/v1/alertSetting/update")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("알림상태는 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("행운의 한마디 알림시간 수정")
    @Test
    @WithMockUser("USER")
    void updateLuckMessageAlertTime() throws Exception {
        // given
        AlertSettingLuckMessageAlertTimeRequest request = AlertSettingLuckMessageAlertTimeRequest.builder()
                .luckMessageAlertTime(LocalTime.of(8,0))
                .deviceId("testdevice")
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/v1/alertSetting/luckMessageAlertTime/update")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("행운의 한마디 알림시간 수정시 알림시간은 필수이다.")
    @Test
    @WithMockUser("USER")
    void updateLuckMessageAlertTimeWithoutAlertTIme() throws Exception {
        // given
        AlertSettingLuckMessageAlertTimeRequest request = AlertSettingLuckMessageAlertTimeRequest.builder()
                .deviceId("testdevice")
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/v1/alertSetting/luckMessageAlertTime/update")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("알림 시간은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("행운의 한마디 알림시간 수정시 디바이스ID는 필수이다.")
    @Test
    @WithMockUser("USER")
    void updateLuckMessageAlertTimeWithoutDeviceId() throws Exception {
        // given
        AlertSettingLuckMessageAlertTimeRequest request = AlertSettingLuckMessageAlertTimeRequest.builder()
                .luckMessageAlertTime(LocalTime.of(8,0))
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/v1/alertSetting/luckMessageAlertTime/update")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("디바이스ID는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
