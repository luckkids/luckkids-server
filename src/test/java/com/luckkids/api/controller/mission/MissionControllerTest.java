package com.luckkids.api.controller.mission;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.mission.request.MissionCreateRequest;
import com.luckkids.api.controller.mission.request.MissionUpdateRequest;
import com.luckkids.jwt.dto.UserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalTime;

import static com.luckkids.domain.misson.AlertStatus.CHECKED;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MissionControllerTest extends ControllerTestSupport {

    @DisplayName("신규 미션을 등록한다.")
    @Test
    @WithMockUser(roles = "USER")
    void createMission() throws Exception {
        // given
        MissionCreateRequest request = MissionCreateRequest.builder()
            .missionDescription("운동하기")
            .alertStatus(CHECKED)
            .alertTime(LocalTime.of(18, 30))
            .build();

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo());

        // when // then
        mockMvc.perform(
                post("/api/v1/missions/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.statusCode").value("201"))
            .andExpect(jsonPath("$.httpStatus").value("CREATED"))
            .andExpect(jsonPath("$.message").value("CREATED"));
    }

    @DisplayName("신규 미션을 등록할 때 미션 내용은 필수다.")
    @Test
    @WithMockUser(roles = "USER")
    void createMissionWithoutMissionDescription() throws Exception {
        // given
        MissionCreateRequest request = MissionCreateRequest.builder()
            .alertStatus(CHECKED)
            .alertTime(LocalTime.of(0, 0))
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/missions/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("미션 내용은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 미션을 등록할 때 알림 여부는 필수다.")
    @Test
    @WithMockUser(roles = "USER")
    void createMissionWithoutAlertStatus() throws Exception {
        // given
        MissionCreateRequest request = MissionCreateRequest.builder()
            .missionDescription("T")
            .alertTime(LocalTime.of(0, 0))
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/missions/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("알람 여부는 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 미션을 등록할 때 알림 시간은 필수다.")
    @Test
    @WithMockUser(roles = "USER")
    void createMissionWithoutAlertTime() throws Exception {
        // given
        MissionUpdateRequest request = MissionUpdateRequest.builder()
            .missionDescription("T")
            .alertStatus(CHECKED)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/missions/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("알람 시간은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("등록되어있는 미션을 수정한다.")
    @Test
    @WithMockUser(roles = "USER")
    void updateMission() throws Exception {
        // given
        MissionUpdateRequest request = MissionUpdateRequest.builder()
            .missionDescription("운동하기")
            .alertStatus(CHECKED)
            .alertTime(LocalTime.of(18, 30))
            .build();

        // when // then
        mockMvc.perform(
                patch("/api/v1/missions/{missionId}", 1)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("등록되어있는 미션을 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void getMission() throws Exception {
        //given
        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo());

        // when // then
        mockMvc.perform(
                get("/api/v1/missions")
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));

    }

    private UserInfo createUserInfo() {
        return UserInfo.builder()
            .userId(1)
            .email("")
            .build();
    }
}