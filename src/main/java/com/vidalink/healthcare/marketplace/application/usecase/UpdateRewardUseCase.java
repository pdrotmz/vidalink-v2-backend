package com.vidalink.healthcare.marketplace.application.usecase;

import com.vidalink.healthcare.marketplace.application.dto.request.UpdateRewardRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.RewardResponse;

import java.util.UUID;

public interface UpdateRewardUseCase {

    RewardResponse execute(UUID id, UpdateRewardRequest request);
}
