package com.vidalink.healthcare.marketplace.application.usecase.redemption;

import com.vidalink.healthcare.marketplace.application.dto.request.redemption.CreateRedemptionRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;

public interface CreateRedemptionUseCase {

    RedemptionResponse execute(CreateRedemptionRequest request);
}
