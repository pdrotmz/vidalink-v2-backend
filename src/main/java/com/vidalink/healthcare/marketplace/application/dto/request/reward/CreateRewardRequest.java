package com.vidalink.healthcare.marketplace.application.dto.request.reward;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRewardRequest(

        @NotBlank(message = "name must be filled") String name,
        @NotBlank(message = "description must be filled") String description,
        @NotNull(message = "pointCost must be filled") int pointCost,
        @NotNull(message = "stock must be filled") int stock
) {
}
