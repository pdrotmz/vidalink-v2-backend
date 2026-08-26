package com.vidalink.healthcare.gamification.application.dto.response.points;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record UserPointsResponse(

        @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID userId,

        @Schema(description = "User points", example = "500")
        Integer balance
) {
}
