package com.vidalink.healthcare.gamification.application.usecase.pointtransaction;

import com.vidalink.healthcare.gamification.entity.dto.response.pointtransaction.PointTransactionResponse;


import java.util.List;
import java.util.UUID;

public interface GetUserPointTransactionsUseCase {

    List<PointTransactionResponse> execute(UUID userId);
}
