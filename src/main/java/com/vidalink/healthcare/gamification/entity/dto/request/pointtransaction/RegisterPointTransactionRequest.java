package com.vidalink.healthcare.gamification.entity.dto.request.pointtransaction;

import com.vidalink.healthcare.gamification.entity.enums.pointtransaction.PointTransactionSource;
import com.vidalink.healthcare.gamification.entity.enums.pointtransaction.PointTransactionType;

import java.util.UUID;

public record RegisterPointTransactionRequest(
        UUID userId,
        Integer amount,
        PointTransactionType type,
        PointTransactionSource source
) {
}
