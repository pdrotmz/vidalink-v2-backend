package com.vidalink.healthcare.shared.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ErrorResponse(

    @Schema(description = "Status code", example = "404")
    int status,

    @Schema(description = "Error type", example = "NOT FOUND")
    String error,

    @Schema(description = "Error message", example = "Reward was not found")
    String message,

    @Schema(description = "When error occurs", example = "2026-08-25T15:50:45.123456789")
    LocalDateTime timestamp,

    @Schema(description = "Error path", example = "reward/id/123e4567-e89b-12d3-a456-426614174000")
    String path
) {
}
