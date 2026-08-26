package com.vidalink.healthcare.marketplace.application.dto.response.reward;

import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record RewardResponse(

        @Schema(description = "Reward ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Reward name", example = "GIFT CARD LOL")
        String name,

        @Schema(description = "Reward description", example = "LOL GIFT CARD THAT VALUES 500 RP")
        String description,

        @Schema(description = "Reward quantity", example = "20")
        int stock,

        @Schema(description = "Reward value", example = "500 Points")
        int pointCost,

        @Schema(description = "Reward", example = "rewards/2d75a206-040b-4cdc-a7e6-0d4aaccc6cf6")
        String image,

        @Schema(description = "IS reward active", example = "true/false")
        boolean isActive
) {

    public static RewardResponse from(Reward reward) {
        return new RewardResponse(
                reward.getId(),
                reward.getName(),
                reward.getDescription(),
                reward.getStock(),
                reward.getPointsCost(),
                reward.getImagePath(),
                reward.isActive()
        );
    }
}
