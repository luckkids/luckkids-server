package com.luckkids.domain.userCharacter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Level {

    LEVEL_1(1, 0),
    LEVEL_2(2, 25),
    LEVEL_3(3, 50),
    LEVEL_4(4, 75),
    LEVEL_MAX(5, 100);

    private final int level;
    private final int scoreThreshold;

    public static int getLevelByScore(int count) {
        for (Level level : Level.values()) {
            if (level.getScoreThreshold() == count) return level.getLevel();
        }
        return 0;
    }
}