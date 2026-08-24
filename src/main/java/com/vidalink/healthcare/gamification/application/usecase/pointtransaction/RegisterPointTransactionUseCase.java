package com.vidalink.healthcare.gamification.application.usecase.pointtransaction;


import com.vidalink.healthcare.gamification.entity.enums.pointtransaction.PointTransactionSource;
import com.vidalink.healthcare.gamification.entity.enums.pointtransaction.PointTransactionType;

import java.util.UUID;

public interface RegisterPointTransactionUseCase {

    void execute(UUID userId, Integer amount, PointTransactionType type, PointTransactionSource source);
}
