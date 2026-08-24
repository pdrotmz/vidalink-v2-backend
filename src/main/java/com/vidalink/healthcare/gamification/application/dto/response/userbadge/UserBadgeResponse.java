package com.vidalink.healthcare.gamification.application.dto.response.userbadge;

import com.vidalink.healthcare.gamification.domain.enums.badge.Badge;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserBadgeResponse(
        UUID id,
        UUID userId,
        Badge badge,
        LocalDateTime earnedAt
) {
}
