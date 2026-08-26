package com.vidalink.healthcare.marketplace.application.usecase.redemption;

import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;

import java.util.UUID;

public interface GetRedemptionByIdUseCase {

    RedemptionResponse execute(UUID id);
}
