package com.vidalink.healthcare.gamification.application.dto.response.userbadge;

import com.vidalink.healthcare.gamification.domain.enums.badge.Badge;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserBadgeResponse(

        @Schema(description = "User Badge ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID userId,

        @Schema(description = "Badge of an user", example = "MASTER")
        Badge badge,

        @Schema(description = "Register when an user earn a badge", example = "2026-08-25T15:50:45.123456789")
        LocalDateTime earnedAt
) {
}
