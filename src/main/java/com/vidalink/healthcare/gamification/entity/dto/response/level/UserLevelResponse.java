package com.vidalink.healthcare.gamification.entity.dto.response.level;

import com.vidalink.healthcare.gamification.entity.enums.level.Level;

import java.util.UUID;

public record UserLevelResponse(
        UUID userId,
        Level level,
        int totalPoints
) {
}
