package com.vidalink.healthcare.identity.application.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterResponse(

        @Schema(description = "user name", example = "Pedro Tomáz")
        String name,

        @Schema(description = "user email", example = "pedro@gmail.com")
        String email
) {
}
