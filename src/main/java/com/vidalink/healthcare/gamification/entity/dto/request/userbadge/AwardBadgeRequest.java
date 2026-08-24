package com.vidalink.healthcare.gamification.entity.dto.request.userbadge;

import com.vidalink.healthcare.gamification.entity.enums.badge.Badge;
import jakarta.validation.constraints.NotNull;

public record AwardBadgeRequest(
        @NotNull
        Badge badge
) {
}
