package com.vidalink.healthcare.identity.application.dto.response;

import com.vidalink.healthcare.identity.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record MeResponse(

        @Schema(description = "user id", example = "2d75a206-040b-4cdc-a7e6-0d4aaccc6cf6")
        UUID id,

        @Schema(description = "user name", example = "Pedro Tomáz")
        String name,

        @Schema(description = "user email", example = "pedro@gmail.com")
        String email,

        @Schema(description = "user cpf", example = "12345678900")
        String cpf,

        @Schema(description = "user role", example = "CLIENT/ADMIN")
        UserRole role
) {
}
