package com.luckkids.mission.controller.luckkidsMission;

import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.luckkids.ControllerTestSupport;
import com.luckkids.mission.controller.request.LuckkidsMissionListRequest;
import com.luckkids.mission.controller.request.LuckkidsMissionRequest;
import com.luckkids.mission.domain.misson.MissionType;

public class LuckkidsMissionControllerTest extends ControllerTestSupport {

	@DisplayName("럭키즈 습관을 등록한다.")
	@Test
	@WithMockUser(roles = "USER")
	void saveLuckkidsMission() throws Exception {
		// given
		LuckkidsMissionListRequest request = LuckkidsMissionListRequest.builder()
			.missions(
				List.of(
					LuckkidsMissionRequest.builder()
						.missionType(MissionType.HEALTH)
						.missionDescription("아침 일찍 일어나기")
						.alertTime(LocalTime.of(1, 0))
						.sort(1)
						.build(),
					LuckkidsMissionRequest.builder()
						.missionType(MissionType.HOUSEKEEPING)
						.missionDescription("아침에 청소기 돌리기")
						.alertTime(LocalTime.of(1, 0))
						.sort(1)
						.build()
				)
			)
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
		LuckkidsMissionListRequest request = LuckkidsMissionListRequest.builder()
			.missions(
				List.of(
					LuckkidsMissionRequest.builder()
						.missionDescription("아침 일찍 일어나기")
						.alertTime(LocalTime.of(1, 0))
						.sort(1)
						.build(),
					LuckkidsMissionRequest.builder()
						.missionType(MissionType.HOUSEKEEPING)
						.missionDescription("아침에 청소기 돌리기")
						.alertTime(LocalTime.of(1, 0))
						.sort(1)
						.build()
				)
			)
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
		LuckkidsMissionListRequest request = LuckkidsMissionListRequest.builder()
			.missions(
				List.of(
					LuckkidsMissionRequest.builder()
						.missionType(MissionType.HEALTH)
						.alertTime(LocalTime.of(1, 0))
						.sort(1)
						.build(),
					LuckkidsMissionRequest.builder()
						.missionType(MissionType.HOUSEKEEPING)
						.missionDescription("아침에 청소기 돌리기")
						.alertTime(LocalTime.of(1, 0))
						.sort(1)
						.build()
				)
			)
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
		LuckkidsMissionListRequest request = LuckkidsMissionListRequest.builder()
			.missions(
				List.of(
					LuckkidsMissionRequest.builder()
						.missionType(MissionType.HEALTH)
						.missionDescription("아침 일찍 일어나기")
						.alertTime(LocalTime.of(1, 0))
						.sort(1)
						.build(),
					LuckkidsMissionRequest.builder()
						.missionType(MissionType.HOUSEKEEPING)
						.missionDescription("아침에 청소기 돌리기")
						.sort(1)
						.build()
				)
			)
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

	@DisplayName("럭키즈 습관을 등록할 시 정렬기준은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void saveLuckkidsMissionWithoutSort() throws Exception {
		// given
		LuckkidsMissionListRequest request = LuckkidsMissionListRequest.builder()
			.missions(
				List.of(
					LuckkidsMissionRequest.builder()
						.missionType(MissionType.HEALTH)
						.missionDescription("아침 일찍 일어나기")
						.alertTime(LocalTime.of(1, 0))
						.sort(-1)
						.build(),
					LuckkidsMissionRequest.builder()
						.missionType(MissionType.HOUSEKEEPING)
						.missionDescription("아침에 청소기 돌리기")
						.alertTime(LocalTime.of(1, 0))
						.sort(1)
						.build()
				)
			)
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
			.andExpect(jsonPath("$.message").value("정렬값은 0 이상이어야 합니다."));
	}

}
