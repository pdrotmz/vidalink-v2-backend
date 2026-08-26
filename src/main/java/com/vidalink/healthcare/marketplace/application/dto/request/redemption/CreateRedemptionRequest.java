package com.vidalink.healthcare.marketplace.application.dto.request.redemption;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateRedemptionRequest(

        @Schema(description = "Reward ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull(message = "idReward must be filled")
        UUID idReward,

        @Schema(description = "Redemption quantity", example = "20")
        @NotNull(message = "quantity must be filled")
        int quantity
) {
}
