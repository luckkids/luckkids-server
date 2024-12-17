package com.luckkids.api.service.luckkidsMission;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.List;

import com.luckkids.api.controller.luckkidsMission.request.LuckkidsMissionListRequest;
import com.luckkids.api.service.luckkidsMission.request.LuckkidsMissionListServiceRequest;
import com.luckkids.api.service.luckkidsMission.response.LuckkidsMissionListSaveResponse;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.luckkidsMission.request.LuckkidsMissionServiceRequest;
import com.luckkids.api.service.luckkidsMission.response.LuckkidsMissionSaveResponse;
import com.luckkids.domain.luckkidsMission.LuckkidsMissionRepository;
import com.luckkids.domain.misson.MissionType;

public class LuckkidsServiceTest extends IntegrationTestSupport {

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
        LuckkidsMissionListServiceRequest luckkidsMissionListServiceRequest = LuckkidsMissionListServiceRequest.builder()
                .missions(List.of(
                        LuckkidsMissionServiceRequest.builder()
                                .missionType(MissionType.HEALTH)
                                .missionDescription("9시에 기상한다.")
                                .alertTime(LocalTime.of(10, 0))
                                .sort(1)
                                .build(),
                        LuckkidsMissionServiceRequest.builder()
                                .missionType(MissionType.HOUSEKEEPING)
                                .missionDescription("아침에 청소기 돌리기")
                                .alertTime(LocalTime.of(10, 0))
                                .sort(1)
                                .build())
                )
                .build();

        LuckkidsMissionListSaveResponse luckkidsMissionListSaveResponse = luckkidsMissionService.createLuckkidsMission(
                luckkidsMissionListServiceRequest);

        assertThat(luckkidsMissionListSaveResponse.getMissions())
                .extracting("missionType", "missionDescription", "alertTime", "sort")
                .contains(
                        new Tuple(MissionType.HEALTH, "9시에 기상한다.", LocalTime.of(10, 0), 1),
                        new Tuple(MissionType.HOUSEKEEPING, "아침에 청소기 돌리기", LocalTime.of(10, 0), 1)
                );
    }

}
