package com.vidalink.healthcare.gamification.application.dto.response.pointtransaction;

import com.vidalink.healthcare.gamification.domain.model.pointtransaction.PointTransaction;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionSource;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record PointTransactionResponse(

        @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        UUID userId,

        @Schema(description = "Amount of Transaction", example = "50")
        Integer amount,

        @Schema(description = "Type of point transaction", example = "CREDIT/DEBIT")
        PointTransactionType type,

        @Schema(description = "Source of point transaction", example = "ASSESSMENT(SUBMISSION)/MARKETPLACE(REDEMPTION)")
        PointTransactionSource source,

        @Schema(description = "Register when point transaction worked", example = "2026-08-25T15:50:45.123456789")
        LocalDateTime createdAt
) {

    public static PointTransactionResponse from(PointTransaction transaction) {
        return new PointTransactionResponse(
                transaction.getId(),
                transaction.getUserId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getSource(),
                transaction.getCreatedAt()
        );
    }
}
