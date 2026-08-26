package com.vidalink.healthcare.identity.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @Schema(description = "user name", example = "Pedro Tomáz")
        @NotBlank(message = "name must be filled")
        String name,

        @Schema(description = "User email", example = "usern@gmail.com")
        @NotBlank(message = "email must be filled")
        @Email
        String email,

        @Schema(description = "user password", example = "Pedrotomaz@3211")
        @NotBlank(message = "password must be filled")
        @Size(min = 8, message = "password must be at least 8 characters")
        String password,

        @Schema(description = "user cpf", example = "41565138713")
        @NotBlank(message = "CPF must be filled")
        @Size(min = 11, max = 11)
        String cpf
) {
}
