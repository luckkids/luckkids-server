package com.luckkids.api.service.luckMission;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.luckMission.response.LuckMissionResponse;
import com.luckkids.domain.luckMission.LuckMission;
import com.luckkids.domain.luckMission.LuckMissionRespository;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.user.User;
import com.luckkids.domain.userCharacter.UserCharacter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;

import static com.luckkids.domain.misson.AlertStatus.CHECKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class LuckMissionReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private LuckMissionReadService luckMissionReadService;
    @Autowired
    private LuckMissionRespository luckMissionRespository;

    @AfterEach
    void tearDown() {
        luckMissionRespository.deleteAllInBatch();
    }

    @DisplayName("럭키즈에서 저장해둔 미션들을 가져온다.")
    @Test
    void getLuckMissions(){
        IntStream.rangeClosed(1, 10).forEach(i -> {
            luckMissionRespository.save(createLuckMission("테스트미션"+i, LocalTime.of(i, 0)));
        });

        List<LuckMissionResponse> luckMissionResponses = luckMissionReadService.getLuckMissions();

        assertThat(luckMissionResponses).hasSize(10)
            .extracting("description", "alertTime")
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

    private LuckMission createLuckMission(String description, LocalTime alertTime){
        return LuckMission.builder()
            .description(description)
            .alertTime(alertTime)
            .build();
    }
}