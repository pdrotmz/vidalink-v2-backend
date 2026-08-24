package com.vidalink.healthcare.gamification.application.dto.response.points;

import java.util.UUID;

public record UserPointsResponse(
        UUID userId,
        Integer balance
) {
}
