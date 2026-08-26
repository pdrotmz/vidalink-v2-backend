package com.vidalink.healthcare.marketplace.application.dto.request.reward;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateRewardRequest(

        @Schema(description = "Reward name", example = "GIFT CARD LOL")
        @NotBlank(message = "name must be filled")
        String name,


        @Schema(description = "Reward description", example = "LOL GIFT CARD THAT VALUES 500 RP")
        @NotBlank(message = "description must be filled")
        String description,


        @Schema(description = "Reward quantity", example = "20")
        @NotNull(message = "stock must be filled")
        int stock
) {
}

