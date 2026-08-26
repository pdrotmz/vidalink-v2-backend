package com.vidalink.healthcare.marketplace.application.dto.response.redemption;

import com.vidalink.healthcare.marketplace.domain.model.redemption.Redemption;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record RedemptionResponse(

        @Schema(description = "Redemption ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID idUser,

        @Schema(description = "Reward ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID idReward,

        @Schema(description = "Redemption quantity", example = "30")
        int quantity,

        @Schema(description = "When redemption was created", example = "2026-08-25T15:50:45.123456789")
        LocalDateTime createdAt
) {

    public static RedemptionResponse from(Redemption redemption) {
        return new RedemptionResponse(
                redemption.getId(),
                redemption.getIdUser(),
                redemption.getIdReward(),
                redemption.getQuantity(),
                redemption.getCreatedAt()
        );
    }
}
