package com.vidalink.healthcare.marketplace.application.dto.request.redemption;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateRedemptionRequest(

        @NotNull(message = "idReward must be filled")
        UUID idReward,

        @NotNull(message = "quantity must be filled")
        int quantity
) {
}
