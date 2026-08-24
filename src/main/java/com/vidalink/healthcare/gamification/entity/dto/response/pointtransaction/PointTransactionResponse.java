package com.vidalink.healthcare.gamification.entity.dto.response.pointtransaction;

import com.vidalink.healthcare.gamification.entity.domain.pointtransaction.PointTransaction;
import com.vidalink.healthcare.gamification.entity.enums.pointtransaction.PointTransactionSource;
import com.vidalink.healthcare.gamification.entity.enums.pointtransaction.PointTransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

public record PointTransactionResponse(
        UUID id,
        UUID userId,
        Integer amount,
        PointTransactionType type,
        PointTransactionSource source,
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
