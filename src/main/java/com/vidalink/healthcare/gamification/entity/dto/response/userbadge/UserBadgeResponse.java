package com.vidalink.healthcare.gamification.entity.dto.response.userbadge;

import com.vidalink.healthcare.gamification.entity.enums.badge.Badge;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserBadgeResponse(
        UUID id,
        UUID userId,
        Badge badge,
        LocalDateTime earnedAt
) {
}
