package com.luckkids.domain.userCharacter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LevelTest {

    @DisplayName("missionCount를 받았을 때 scoreThreshold와 같다면 level을 가져온다.")
    @Test
    void getLevelByScore() {
        // given
        int missionCount24 = 24;
        int missionCount25 = 25;
        int missionCount100 = 100;

        // when
        int levelNone = Level.getLevelByScore(missionCount24);
        int level2 = Level.getLevelByScore(missionCount25);
        int levelMax = Level.getLevelByScore(missionCount100);

        // then
        assertThat(levelNone).isEqualTo(0);
        assertThat(level2).isEqualTo(Level.LEVEL_2.getLevel());
        assertThat(levelMax).isEqualTo(Level.LEVEL_MAX.getLevel());
    }
}