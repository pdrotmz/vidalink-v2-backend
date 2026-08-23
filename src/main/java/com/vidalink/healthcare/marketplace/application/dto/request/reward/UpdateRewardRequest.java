package com.vidalink.healthcare.marketplace.application.dto.request.reward;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateRewardRequest(

        @NotBlank(message = "name must be filled") String name,
        @NotBlank(message = "description must be filled") String description,
        @NotNull(message = "stock must be filled") int stock
) {
}

