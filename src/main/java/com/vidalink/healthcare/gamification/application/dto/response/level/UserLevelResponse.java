package com.vidalink.healthcare.gamification.application.dto.response.level;

import com.vidalink.healthcare.gamification.domain.enums.level.Level;

import java.util.UUID;

public record UserLevelResponse(
        UUID userId,
        Level level,
        int totalPoints
) {
}
