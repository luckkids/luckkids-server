package com.luckkids.domain.userCharacter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LevelTest {

    @DisplayName("missionCount를 받았을 때 scoreThreshold와 같다면 level을 가져온다.")
    @Test
    void getLevelByScore() {
        // given
        int missionCount19 = 19;
        int missionCount20 = 20;
        int missionCount100 = 100;

        // when
        int level0_1 = Level.getLevelByScore(missionCount19);
        int level1 = Level.getLevelByScore(missionCount20);
        int levelMax = Level.getLevelByScore(missionCount100);

        // then
        assertThat(level0_1).isEqualTo(0);
        assertThat(level1).isEqualTo(Level.LEVEL_1.getLevel());
        assertThat(levelMax).isEqualTo(Level.LEVEL_MAX.getLevel());
    }
}