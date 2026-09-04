package com.vidalink.healthcare.identity.application.dto.response;

import com.vidalink.healthcare.identity.domain.enums.UserRole;
import com.vidalink.healthcare.identity.domain.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(

        @Schema(description = "user id", example = "2d75a206-040b-4cdc-a7e6-0d4aaccc6f6")
        UUID id,

        @Schema(description = "user name", example = "Pedro Tomáz")
        String name,

        @Schema(description = "user email", example = "pedro@gmail.com")
        String email,

        @Schema(description = "user CPF", example = "12345678901")
        String cpf,

        @Schema(description = "user role", example = "CLIENT")
        UserRole role,

        @Schema(description = "user creation date")
        LocalDateTime createdAt,

        @Schema(description = "user last update date")
        LocalDateTime updatedAt
) {

        public static UserResponse from(User user) {
                return new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getCpf(),
                        user.getRole(),
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                );
        }
}
