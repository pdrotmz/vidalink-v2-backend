package com.vidalink.healthcare.gamification.application.dto.request.pointtransaction;

import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionSource;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegisterPointTransactionRequest(

        @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull(message = "User ID must be filled")
        UUID userId,

        @Schema(description = "Amount of Transaction", example = "50")
        @NotNull(message = "Amount must be filled")
        Integer amount,

        @Schema(description = "Type of point transaction", example = "CREDIT/DEBIT")
        @NotNull(message = "Type must be filled")
        PointTransactionType type,

        @Schema(description = "Source of point transaction", example = "ASSESSMENT(SUBMISSION)/MARKETPLACE(REDEMPTION)")
        @NotNull(message = "Source must be filled")
        PointTransactionSource source
) {
}
