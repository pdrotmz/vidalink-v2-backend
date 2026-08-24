package com.vidalink.healthcare.gamification.entity.dto.response.points;

import java.util.UUID;

public record UserPointsResponse(
        UUID userId,
        Integer balance
) {
}
