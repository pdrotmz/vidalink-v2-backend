package com.vidalink.healthcare.marketplace.application.dto.request.redemption;

import java.util.UUID;

public record CreateRedemptionRequest(
        UUID idUser,
        UUID idReward,
        int amount
) {
}
