package com.luckkids.domain.userCharacter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Level {

    LEVEL_1(1, 20),
    LEVEL_2(2, 40),
    LEVEL_3(3, 60),
    LEVEL_4(4, 80),
    LEVEL_MAX(5, 100);

    private final int level;
    private final int scoreThreshold;

    public static int getLevelByScore(int count) {
        for (Level level : Level.values()) {
            if (level.getScoreThreshold() == count) {
                return level.getLevel();
            }
        }
        return 0;
    }
}