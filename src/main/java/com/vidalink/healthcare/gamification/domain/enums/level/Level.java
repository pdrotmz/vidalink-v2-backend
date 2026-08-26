package com.vidalink.healthcare.gamification.domain.enums.level;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Level {

    BEGINNER(1, 0),
    CONTRIBUTOR(2, 500),
    ADVANCED(3, 1000),
    EXPERT(4, 2000),
    MASTER(5, 5000);

    private final int number;
    private final int requiredPoints;
}
