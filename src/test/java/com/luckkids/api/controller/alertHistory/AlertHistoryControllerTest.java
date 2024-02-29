package com.luckkids.api.controller.alertHistory;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.alertHistory.request.AlertHistoryDeviceIdRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AlertHistoryControllerTest extends ControllerTestSupport {

    @DisplayName("deviceId를 받고 알림 내역을 가져온다.")
    @Test
    @WithMockUser("USER")
    void getAlertHistory() throws Exception {
        // given
        AlertHistoryDeviceIdRequest request = AlertHistoryDeviceIdRequest.builder()
            .deviceId("testdeviceId")
            .build();

        // when // then
        mockMvc.perform(
                get("/api/v1/alertHistories")
                    .param("deviceId", request.getDeviceId())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("알림 내역을 가져올 때 디바이스 ID는 필수다.")
    @Test
    @WithMockUser("USER")
    void getAlertHistoryWithoutDeviceId() throws Exception {
        // given
        AlertHistoryDeviceIdRequest request = AlertHistoryDeviceIdRequest.builder()
            .build();

        // when // then
        mockMvc.perform(
                get("/api/v1/alertHistories")
                    .param("deviceId", request.getDeviceId())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("디바이스 ID는 필수입니다."));
    }

    @DisplayName("알림 내역 ID를 받아 알림 내역 상태를 업데이트한다.")
    @Test
    @WithMockUser(roles = "USER")
    void updateAlertHistoryStatus() throws Exception {
        // given
        Long id = 1L;

        // when // then
        mockMvc.perform(
                patch("/api/v1/alertHistories/{id}", id)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }
}