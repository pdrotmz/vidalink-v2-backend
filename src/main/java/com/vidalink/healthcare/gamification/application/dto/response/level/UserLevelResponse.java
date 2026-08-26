package com.vidalink.healthcare.gamification.application.dto.response.level;

import com.vidalink.healthcare.gamification.domain.enums.level.Level;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record UserLevelResponse(

        @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID userId,

        @Schema(description = "User Level", example = "EXPERT")
        Level level,

        @Schema(description = "User total points after redeem or submission approved", example = "50")
        int totalPoints
) {
}
