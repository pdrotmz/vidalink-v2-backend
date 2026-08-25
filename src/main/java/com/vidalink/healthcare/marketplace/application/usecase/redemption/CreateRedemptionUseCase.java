package com.vidalink.healthcare.marketplace.application.usecase.redemption;

import com.vidalink.healthcare.marketplace.application.dto.request.redemption.CreateRedemptionRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;

import java.util.UUID;

public interface CreateRedemptionUseCase {

    RedemptionResponse execute(UUID userId, CreateRedemptionRequest request);}
