package com.vidalink.healthcare.gamification.application.dto.request.pointtransaction;

import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionSource;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionType;

import java.util.UUID;

public record RegisterPointTransactionRequest(
        UUID userId,
        Integer amount,
        PointTransactionType type,
        PointTransactionSource source
) {
}
