package com.vidalink.healthcare.gamification.application.dto.request.userbadge;

import com.vidalink.healthcare.gamification.domain.enums.badge.Badge;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AwardBadgeRequest(

        @Schema(description = "Badge of someone", example = "CONTRIBUTOR")
        @NotNull(message = "Badge must be filled")
        Badge badge
) {
}
