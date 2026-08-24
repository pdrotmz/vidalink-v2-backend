package com.vidalink.healthcare.gamification.entity.enums.badge;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Badge {

    FIRST_CONTRIBUTION("First Contribution"),
    CONTRIBUTOR("Contributor"),
    POINT_COLLECTOR("Point Collector");

    private final String name;
}
