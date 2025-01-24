package com.luckkids.api.service.luckMission;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.mission.service.response.LuckkidsMissionResponse;
import com.luckkids.mission.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.mission.infra.LuckkidsMissionRepository;
import com.luckkids.mission.service.LuckkidsMissionReadService;

public class LuckMissionReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private LuckkidsMissionReadService luckkidsMissionReadService;

	@Autowired
	private LuckkidsMissionRepository luckkidsMissionRepository;

	@AfterEach
	void tearDown() {
		luckkidsMissionRepository.deleteAllInBatch();
	}

	@DisplayName("럭키즈에서 저장해둔 미션들을 가져온다.")
	@Test
	void getLuckMissions() {
		IntStream.rangeClosed(1, 10).forEach(i -> {
			luckkidsMissionRepository.save(createLuckMission("테스트미션" + i, LocalTime.of(i, 0)));
		});

		List<LuckkidsMissionResponse> luckMissionResponses = luckkidsMissionReadService.getLuckMissions();

		assertThat(luckMissionResponses).hasSize(10)
			.extracting("missionDescription", "alertTime")
			.containsExactlyInAnyOrder(
				tuple("테스트미션1", LocalTime.of(1, 0)),
				tuple("테스트미션2", LocalTime.of(2, 0)),
				tuple("테스트미션3", LocalTime.of(3, 0)),
				tuple("테스트미션4", LocalTime.of(4, 0)),
				tuple("테스트미션5", LocalTime.of(5, 0)),
				tuple("테스트미션6", LocalTime.of(6, 0)),
				tuple("테스트미션7", LocalTime.of(7, 0)),
				tuple("테스트미션8", LocalTime.of(8, 0)),
				tuple("테스트미션9", LocalTime.of(9, 0)),
				tuple("테스트미션10", LocalTime.of(10, 0))
			);
	}

	private LuckkidsMission createLuckMission(String description, LocalTime alertTime) {
		return LuckkidsMission.builder()
			.missionDescription(description)
			.alertTime(alertTime)
			.build();
	}
}
