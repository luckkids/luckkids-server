package com.luckkids.api.controller.version;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.missionOutcome.request.MissionOutcomeUpdateRequest;
import com.luckkids.api.controller.version.request.VersionSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static com.luckkids.domain.missionOutcome.MissionStatus.SUCCEED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VersionControllerTest extends ControllerTestSupport {

    @DisplayName("앱의 최신버전을 조회한다.")
    @Test
    @WithMockUser(roles = "USER")
    void selectCurrentVersion() throws Exception {
        // given

        // when // then
        mockMvc.perform(
                get("/api/v1/versions")
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("앱의 최신버전을 저장한다.")
    @Test
    @WithMockUser(roles = "USER")
    void saveCurrentVersion() throws Exception {
        // given
        VersionSaveRequest request = VersionSaveRequest.builder()
            .versionNum("1.1.3")
            .url("www.test.com")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/versions")
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

    @DisplayName("앱의 최신버전을 저장할 시 버전정보는 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void saveCurrentVersionWithoutVersion() throws Exception {
        // given
        VersionSaveRequest request = VersionSaveRequest.builder()
            .url("www.test.com")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/versions")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("최신버전은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("앱의 최신버전을 저장할 시 URL은 필수이다.")
    @Test
    @WithMockUser(roles = "USER")
    void saveCurrentVersionWithoutUrl() throws Exception {
        // given
        VersionSaveRequest request = VersionSaveRequest.builder()
                .versionNum("1.1.3")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/versions")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("최신버전 저장 시 URL이 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
