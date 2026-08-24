package com.vidalink.healthcare.gamification.application.dto.request.userbadge;

import com.vidalink.healthcare.gamification.domain.enums.badge.Badge;
import jakarta.validation.constraints.NotNull;

public record AwardBadgeRequest(
        @NotNull
        Badge badge
) {
}
