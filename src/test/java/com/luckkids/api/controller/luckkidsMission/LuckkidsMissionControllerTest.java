package com.luckkids.api.controller.luckkidsMission;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.luckkidsMission.request.LuckkidsMissionRequest;
import com.luckkids.domain.misson.MissionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LuckkidsMissionControllerTest extends ControllerTestSupport {

    @DisplayName("럭키즈 습관을 등록한다.")
    @Test
    @WithMockUser(roles = "USER")
    void saveLuckkidsMission() throws Exception {
        // given
        LuckkidsMissionRequest request = LuckkidsMissionRequest.builder()
            .missionType(MissionType.HEALTH)
            .missionDescription("9시에 기상한다.")
            .alertTime(LocalTime.of(10, 0))
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/luckkidsMission")
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

    @DisplayName("럭키즈 습관을 등록할 시 미션타입은 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void saveLuckkidsMissionWithoutMissionType() throws Exception {
        // given
        LuckkidsMissionRequest request = LuckkidsMissionRequest.builder()
            .missionDescription("9시에 기상한다.")
            .alertTime(LocalTime.of(10, 0))
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/luckkidsMission")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("미션타입은 필수입니다."));
    }

    @DisplayName("럭키즈 습관을 등록할 시 미션설명은 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void saveLuckkidsMissionWithoutDescription() throws Exception {
        // given
        LuckkidsMissionRequest request = LuckkidsMissionRequest.builder()
            .missionType(MissionType.HEALTH)
            .alertTime(LocalTime.of(10, 0))
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/luckkidsMission")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("미션설명은 필수입니다."));
    }

    @DisplayName("럭키즈 습관을 등록할 시 알림시간은 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void saveLuckkidsMissionWithoutAlertTime() throws Exception {
        // given
        LuckkidsMissionRequest request = LuckkidsMissionRequest.builder()
            .missionType(MissionType.HEALTH)
            .missionDescription("9시에 기상한다.")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/luckkidsMission")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("알림시간은 필수입니다."));
    }

}
