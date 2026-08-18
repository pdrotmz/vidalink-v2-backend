package com.vidalink.healthcare.marketplace.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateRewardRequest(

        @NotBlank(message = "name must be filled") String name,
        @NotBlank(message = "description must be filled") String description,
        @NotNull(message = "stock must be filled") int stock
) {
}
