package com.luckkids.api.service.luckkidsMission;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.luckkidsMission.request.LuckkidsMissionServiceRequest;
import com.luckkids.api.service.luckkidsMission.response.LuckkidsMissionSaveResponse;
import com.luckkids.api.service.push.PushService;
import com.luckkids.domain.luckkidsMission.LuckkidsMissionRepository;
import com.luckkids.domain.misson.MissionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

public class LuckkidsServiceTest extends IntegrationTestSupport {

    @MockBean
    private PushService pushService;

    @Autowired
    private LuckkidsMissionService luckkidsMissionService;

    @Autowired
    private LuckkidsMissionRepository luckkidsMissionRepository;

    @AfterEach
    void tearDown() {
        luckkidsMissionRepository.deleteAllInBatch();
    }

    @DisplayName("럭키즈 미션을 저장한다.")
    @Test
    void saveLuckkidsMissionTest() {
        LuckkidsMissionServiceRequest luckkidsMissionRequest = LuckkidsMissionServiceRequest.builder()
            .missionType(MissionType.HEALTH)
            .missionDescription("9시에 기상한다.")
            .alertTime(LocalTime.of(10, 0))
            .build();

        LuckkidsMissionSaveResponse luckkidsMissionSaveResponse = luckkidsMissionService.createLuckkidsMission(luckkidsMissionRequest);

        assertThat(luckkidsMissionSaveResponse)
            .extracting("missionType", "missionDescription", "alertTime")
            .contains(
                MissionType.HEALTH, "9시에 기상한다.", LocalTime.of(10, 0)
            );
    }

}
