package com.vidalink.healthcare.identity.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @Schema(description = "User email", example = "CLIENT: user@gmail.com, ADMIN: admin@vidalink.com")
        @Email
        @NotBlank(message = "Email must be filled")
        String email,

        @Schema(description = "User password", example = "Pedrotomaz@2453")
        @NotBlank(message = "Password must be filled")
        String password
) {
}
