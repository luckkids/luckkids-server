package com.luckkids.api.service.luckkidsMission;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.luckkidsMission.response.LuckkidsMissionResponse;
import com.luckkids.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.domain.luckkidsMission.LuckkidsMissionRepository;
import com.luckkids.domain.misson.MissionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

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
            luckkidsMissionRepository.save(createLuckkidsMission("테스트미션" + i, LocalTime.of(i, 0)));
        });

        List<LuckkidsMissionResponse> luckkidsMissionResponses = luckkidsMissionReadService.getLuckMissions();

        assertThat(luckkidsMissionResponses).hasSize(10)
                .extracting("id", "missionType", "missionDescription", "alertTime")
                .containsExactlyInAnyOrder(
                        tuple(1, MissionType.HEALTH, "테스트미션1", LocalTime.of(1, 0)),
                        tuple(2, MissionType.HEALTH, "테스트미션2", LocalTime.of(2, 0)),
                        tuple(3, MissionType.HEALTH, "테스트미션3", LocalTime.of(3, 0)),
                        tuple(4, MissionType.HEALTH, "테스트미션4", LocalTime.of(4, 0)),
                        tuple(5, MissionType.HEALTH, "테스트미션5", LocalTime.of(5, 0)),
                        tuple(6, MissionType.HEALTH, "테스트미션6", LocalTime.of(6, 0)),
                        tuple(7, MissionType.HEALTH, "테스트미션7", LocalTime.of(7, 0)),
                        tuple(8, MissionType.HEALTH, "테스트미션8", LocalTime.of(8, 0)),
                        tuple(9, MissionType.HEALTH, "테스트미션9", LocalTime.of(9, 0)),
                        tuple(10, MissionType.HEALTH, "테스트미션10", LocalTime.of(10, 0))
                );
    }

    private LuckkidsMission createLuckkidsMission(String missionDescription, LocalTime alertTime) {
        return LuckkidsMission.builder()
                .missionType(MissionType.HEALTH)
                .missionDescription(missionDescription)
                .alertTime(alertTime)
                .build();
    }
}
