package com.vidalink.healthcare.marketplace.application.dto.response.redemption;

import com.vidalink.healthcare.marketplace.domain.model.redemption.Redemption;

import java.time.LocalDateTime;
import java.util.UUID;

public record RedemptionResponse(
        UUID id,
        UUID idUser,
        UUID idReward,
        int amount,
        LocalDateTime createdAt
) {

    public static RedemptionResponse from(Redemption redemption) {
        return new RedemptionResponse(
                redemption.getId(),
                redemption.getIdUser(),
                redemption.getIdReward(),
                redemption.getAmount(),
                redemption.getCreatedAt()
        );
    }
}
