package com.luckkids.api.controller.initialSetting;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.initialSetting.request.InitialSettingAlertRequest;
import com.luckkids.api.controller.initialSetting.request.InitialSettingCharacterRequest;
import com.luckkids.api.controller.initialSetting.request.InitialSettingMissionRequest;
import com.luckkids.api.controller.initialSetting.request.InitialSettingRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.luckkids.domain.misson.AlertStatus.CHECKED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class initialSettingControllerTest extends ControllerTestSupport {

    @DisplayName("사용자의 초기세팅 데이터를 저장한다.")
    @Test
    @WithMockUser("USER")
    void createInitialSetting() throws Exception {
        // given
        InitialSettingCharacterRequest initialSettingCharacterRequest = InitialSettingCharacterRequest.builder()
            .characterName("럭키즈")
            .fileName("test.json")
            .build();

        List<InitialSettingMissionRequest> initialSettingMissionRequests = new ArrayList<>();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            initialSettingMissionRequests.add(
                InitialSettingMissionRequest.builder()
                    .missionDescription(i+"시에 운동하기")
                    .alertStatus(CHECKED)
                    .alertTime(LocalTime.of(0,0))
                    .build()
            );
        });

        InitialSettingAlertRequest initialSettingAlertRequest = InitialSettingAlertRequest.builder()
            .alertStatus(CHECKED)
            .build();

        InitialSettingRequest request = InitialSettingRequest.builder()
            .character(initialSettingCharacterRequest)
            .missions(initialSettingMissionRequests)
            .alertSetting(initialSettingAlertRequest)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.statusCode").value("201"))
            .andExpect(jsonPath("$.httpStatus").value("CREATED"))
            .andExpect(jsonPath("$.message").value("CREATED"));
    }

    @DisplayName("사용자의 초기세팅 데이터를 저장시 캐릭터명은 필수이다.")
    @Test
    @WithMockUser("USER")
    void createInitialSettingWithoutCharacterName() throws Exception {
        // given
        InitialSettingCharacterRequest initialSettingCharacterRequest = InitialSettingCharacterRequest.builder()
            .fileName("test.json")
            .build();

        List<InitialSettingMissionRequest> initialSettingMissionRequests = new ArrayList<>();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            initialSettingMissionRequests.add(
                InitialSettingMissionRequest.builder()
                    .missionDescription(i+"시에 운동하기")
                    .alertStatus(CHECKED)
                    .alertTime(LocalTime.of(0,0))
                    .build()
            );
        });

        InitialSettingAlertRequest initialSettingAlertRequest = InitialSettingAlertRequest.builder()
            .alertStatus(CHECKED)
            .build();

        InitialSettingRequest request = InitialSettingRequest.builder()
            .character(initialSettingCharacterRequest)
            .missions(initialSettingMissionRequests)
            .alertSetting(initialSettingAlertRequest)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("캐릭터 이름은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("사용자의 초기세팅 데이터를 저장시 캐릭터 파일명은 필수이다.")
    @Test
    @WithMockUser("USER")
    void createInitialSettingWithoutFileName() throws Exception {
        // given
        InitialSettingCharacterRequest initialSettingCharacterRequest = InitialSettingCharacterRequest.builder()
            .characterName("럭키즈")
            .build();

        List<InitialSettingMissionRequest> initialSettingMissionRequests = new ArrayList<>();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            initialSettingMissionRequests.add(
                InitialSettingMissionRequest.builder()
                    .missionDescription(i+"시에 운동하기")
                    .alertStatus(CHECKED)
                    .alertTime(LocalTime.of(0,0))
                    .build()
            );
        });

        InitialSettingAlertRequest initialSettingAlertRequest = InitialSettingAlertRequest.builder()
            .alertStatus(CHECKED)
            .build();

        InitialSettingRequest request = InitialSettingRequest.builder()
            .character(initialSettingCharacterRequest)
            .missions(initialSettingMissionRequests)
            .alertSetting(initialSettingAlertRequest)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("파일명은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("사용자의 초기세팅 데이터를 저장시 캐릭터 요청값은 필수이다.")
    @Test
    @WithMockUser("USER")
    void createInitialSettingWithoutCharacterRequest() throws Exception {
        // given
        List<InitialSettingMissionRequest> initialSettingMissionRequests = new ArrayList<>();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            initialSettingMissionRequests.add(
                InitialSettingMissionRequest.builder()
                    .missionDescription(i+"시에 운동하기")
                    .alertStatus(CHECKED)
                    .alertTime(LocalTime.of(0,0))
                    .build()
            );
        });

        InitialSettingAlertRequest initialSettingAlertRequest = InitialSettingAlertRequest.builder()
            .alertStatus(CHECKED)
            .build();

        InitialSettingRequest request = InitialSettingRequest.builder()
            .missions(initialSettingMissionRequests)
            .alertSetting(initialSettingAlertRequest)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("캐릭터설정 요청값은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("사용자의 초기세팅 데이터를 저장시 미션내용은 필수이다.")
    @Test
    @WithMockUser("USER")
    void createInitialSettingWithoutMissionDescription() throws Exception {
        // given
        InitialSettingCharacterRequest initialSettingCharacterRequest = InitialSettingCharacterRequest.builder()
            .fileName("test.json")
            .characterName("럭키즈")
            .build();

        List<InitialSettingMissionRequest> initialSettingMissionRequests = new ArrayList<>();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            initialSettingMissionRequests.add(
                InitialSettingMissionRequest.builder()
                    .alertStatus(CHECKED)
                    .alertTime(LocalTime.of(0,0))
                    .build()
            );
        });

        InitialSettingAlertRequest initialSettingAlertRequest = InitialSettingAlertRequest.builder()
            .alertStatus(CHECKED)
            .build();

        InitialSettingRequest request = InitialSettingRequest.builder()
            .character(initialSettingCharacterRequest)
            .missions(initialSettingMissionRequests)
            .alertSetting(initialSettingAlertRequest)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("미션 내용은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("사용자의 초기세팅 데이터를 저장시 미션알림 여부는 필수이다.")
    @Test
    @WithMockUser("USER")
    void createInitialSettingWithoutMissionAlertStatus() throws Exception {
        // given
        InitialSettingCharacterRequest initialSettingCharacterRequest = InitialSettingCharacterRequest.builder()
            .fileName("test.json")
            .characterName("럭키즈")
            .build();

        List<InitialSettingMissionRequest> initialSettingMissionRequests = new ArrayList<>();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            initialSettingMissionRequests.add(
                InitialSettingMissionRequest.builder()
                    .missionDescription(i+"시에 운동하기")
                    .alertTime(LocalTime.of(0,0))
                    .build()
            );
        });

        InitialSettingAlertRequest initialSettingAlertRequest = InitialSettingAlertRequest.builder()
            .alertStatus(CHECKED)
            .build();

        InitialSettingRequest request = InitialSettingRequest.builder()
            .character(initialSettingCharacterRequest)
            .missions(initialSettingMissionRequests)
            .alertSetting(initialSettingAlertRequest)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("알람 여부는 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("사용자의 초기세팅 데이터를 저장시 미션알림시간은 필수이다.")
    @Test
    @WithMockUser("USER")
    void createInitialSettingWithoutMissionAlertTime() throws Exception {
        // given
        InitialSettingCharacterRequest initialSettingCharacterRequest = InitialSettingCharacterRequest.builder()
            .fileName("test.json")
            .characterName("럭키즈")
            .build();

        List<InitialSettingMissionRequest> initialSettingMissionRequests = new ArrayList<>();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            initialSettingMissionRequests.add(
                InitialSettingMissionRequest.builder()
                    .missionDescription(i+"시에 운동하기")
                    .alertStatus(CHECKED)
                    .build()
            );
        });

        InitialSettingAlertRequest initialSettingAlertRequest = InitialSettingAlertRequest.builder()
            .alertStatus(CHECKED)
            .build();

        InitialSettingRequest request = InitialSettingRequest.builder()
            .character(initialSettingCharacterRequest)
            .missions(initialSettingMissionRequests)
            .alertSetting(initialSettingAlertRequest)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("알람 시간은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("사용자의 초기세팅 데이터를 저장시 미션설정 요청값은 필수이다.")
    @Test
    @WithMockUser("USER")
    void createInitialSettingWithoutMissionRequest() throws Exception {
        // given
        InitialSettingCharacterRequest initialSettingCharacterRequest = InitialSettingCharacterRequest.builder()
            .fileName("test.json")
            .characterName("럭키즈")
            .build();

        InitialSettingAlertRequest initialSettingAlertRequest = InitialSettingAlertRequest.builder()
            .alertStatus(CHECKED)
            .build();

        InitialSettingRequest request = InitialSettingRequest.builder()
            .character(initialSettingCharacterRequest)
            .alertSetting(initialSettingAlertRequest)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("미션설정 요청값은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("사용자의 초기세팅 데이터를 저장시 전체 알림 설정여부는 필수이다.")
    @Test
    @WithMockUser("USER")
    void createInitialSettingWithoutAlertStatus() throws Exception {
        // given
        InitialSettingCharacterRequest initialSettingCharacterRequest = InitialSettingCharacterRequest.builder()
            .fileName("test.json")
            .characterName("럭키즈")
            .build();

        List<InitialSettingMissionRequest> initialSettingMissionRequests = new ArrayList<>();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            initialSettingMissionRequests.add(
                InitialSettingMissionRequest.builder()
                    .missionDescription(i+"시에 운동하기")
                    .alertStatus(CHECKED)
                    .alertTime(LocalTime.of(0,0))
                    .build()
            );
        });

        InitialSettingAlertRequest initialSettingAlertRequest = InitialSettingAlertRequest.builder()
            .build();

        InitialSettingRequest request = InitialSettingRequest.builder()
            .character(initialSettingCharacterRequest)
            .missions(initialSettingMissionRequests)
            .alertSetting(initialSettingAlertRequest)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting/new")
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

    @DisplayName("사용자의 초기세팅 데이터를 저장시 전체 알림 요청값은 필수이다.")
    @Test
    @WithMockUser("USER")
    void createInitialSettingWithoutAlertRequest() throws Exception {
        // given
        InitialSettingCharacterRequest initialSettingCharacterRequest = InitialSettingCharacterRequest.builder()
            .fileName("test.json")
            .characterName("럭키즈")
            .build();

        List<InitialSettingMissionRequest> initialSettingMissionRequests = new ArrayList<>();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            initialSettingMissionRequests.add(
                InitialSettingMissionRequest.builder()
                    .missionDescription(i+"시에 운동하기")
                    .alertStatus(CHECKED)
                    .alertTime(LocalTime.of(0,0))
                    .build()
            );
        });

        InitialSettingRequest request = InitialSettingRequest.builder()
            .character(initialSettingCharacterRequest)
            .missions(initialSettingMissionRequests)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("알림설정 요청값은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }
}
