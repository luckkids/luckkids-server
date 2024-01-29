package com.luckkids.api.controller.initialSetting;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.initialSetting.request.InitialSettingAlertRequest;
import com.luckkids.api.controller.initialSetting.request.InitialSettingCharacterRequest;
import com.luckkids.api.controller.initialSetting.request.InitialSettingMissionRequest;
import com.luckkids.api.controller.initialSetting.request.InitialSettingRequest;
import com.luckkids.api.service.luckkidsCharacter.response.InitialCharacterRandResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
            .characterNickname("럭키즈")
            .fileName("test.json")
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
            .deviceId("testDeviceId")
            .alertStatus(CHECKED)
            .build();

        InitialSettingRequest request = InitialSettingRequest.builder()
            .character(initialSettingCharacterRequest)
            .missions(initialSettingMissionRequests)
            .alertSetting(initialSettingAlertRequest)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting")
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
                    .alertTime(LocalTime.of(0,0))
                    .build()
            );
        });

        InitialSettingAlertRequest initialSettingAlertRequest = InitialSettingAlertRequest.builder()
            .deviceId("testDeviceId")
            .alertStatus(CHECKED)
            .build();

        InitialSettingRequest request = InitialSettingRequest.builder()
            .character(initialSettingCharacterRequest)
            .missions(initialSettingMissionRequests)
            .alertSetting(initialSettingAlertRequest)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("캐릭터 닉네임은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("사용자의 초기세팅 데이터를 저장시 캐릭터 파일명은 필수이다.")
    @Test
    @WithMockUser("USER")
    void createInitialSettingWithoutFileName() throws Exception {
        // given
        InitialSettingCharacterRequest initialSettingCharacterRequest = InitialSettingCharacterRequest.builder()
            .characterNickname("럭키즈")
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
            .deviceId("testDeviceId")
            .alertStatus(CHECKED)
            .build();

        InitialSettingRequest request = InitialSettingRequest.builder()
            .character(initialSettingCharacterRequest)
            .missions(initialSettingMissionRequests)
            .alertSetting(initialSettingAlertRequest)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting")
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
                    .alertTime(LocalTime.of(0,0))
                    .build()
            );
        });

        InitialSettingAlertRequest initialSettingAlertRequest = InitialSettingAlertRequest.builder()
            .deviceId("testDeviceId")
            .alertStatus(CHECKED)
            .build();

        InitialSettingRequest request = InitialSettingRequest.builder()
            .missions(initialSettingMissionRequests)
            .alertSetting(initialSettingAlertRequest)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting")
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
            .characterNickname("럭키즈")
            .build();

        List<InitialSettingMissionRequest> initialSettingMissionRequests = new ArrayList<>();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            initialSettingMissionRequests.add(
                InitialSettingMissionRequest.builder()
                    .alertTime(LocalTime.of(0,0))
                    .build()
            );
        });

        InitialSettingAlertRequest initialSettingAlertRequest = InitialSettingAlertRequest.builder()
            .deviceId("testDeviceId")
            .alertStatus(CHECKED)
            .build();

        InitialSettingRequest request = InitialSettingRequest.builder()
            .character(initialSettingCharacterRequest)
            .missions(initialSettingMissionRequests)
            .alertSetting(initialSettingAlertRequest)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting")
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


    @DisplayName("사용자의 초기세팅 데이터를 저장시 미션알림시간은 필수이다.")
    @Test
    @WithMockUser("USER")
    void createInitialSettingWithoutMissionAlertTime() throws Exception {
        // given
        InitialSettingCharacterRequest initialSettingCharacterRequest = InitialSettingCharacterRequest.builder()
            .fileName("test.json")
            .characterNickname("럭키즈")
            .build();

        List<InitialSettingMissionRequest> initialSettingMissionRequests = new ArrayList<>();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            initialSettingMissionRequests.add(
                InitialSettingMissionRequest.builder()
                    .missionDescription(i+"시에 운동하기")
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
                post("/api/v1/initialSetting")
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
            .characterNickname("럭키즈")
            .build();

        InitialSettingAlertRequest initialSettingAlertRequest = InitialSettingAlertRequest.builder()
            .deviceId("testDeviceId")
            .alertStatus(CHECKED)
            .build();

        InitialSettingRequest request = InitialSettingRequest.builder()
            .character(initialSettingCharacterRequest)
            .alertSetting(initialSettingAlertRequest)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting")
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
            .characterNickname("럭키즈")
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
            .deviceId("testDeviceId")
            .build();

        InitialSettingRequest request = InitialSettingRequest.builder()
            .character(initialSettingCharacterRequest)
            .missions(initialSettingMissionRequests)
            .alertSetting(initialSettingAlertRequest)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting")
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
            .characterNickname("럭키즈")
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

        InitialSettingRequest request = InitialSettingRequest.builder()
            .character(initialSettingCharacterRequest)
            .missions(initialSettingMissionRequests)
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/initialSetting")
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

    @DisplayName("초기 캐릭터를 모두 조회한다.")
    @Test
    @WithMockUser("USER")
    void findAllInitialCharacter() throws Exception {
        // given
        List<InitialCharacterRandResponse> initialCharacterRandResponses = new ArrayList<>();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            initialCharacterRandResponses.add(
                InitialCharacterRandResponse.builder()
                    .characterName("테스트"+i)
                    .fileName("test"+i+".json")
                    .build()
            );
        });

        // when // then
        mockMvc.perform(
                get("/api/v1/initialSetting/character")
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

    @DisplayName("럭키즈가 등록해둔 미션조회 API")
    @Test
    @WithMockUser(roles = "USER")
    void getLuckMission() throws Exception {
        // given

        // when // then
        mockMvc.perform(
                get("/api/v1/initialSetting/luckMission")
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }
}
