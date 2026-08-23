package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.request.reward.UpdateRewardRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;

import java.util.UUID;

public interface UpdateRewardUseCase {

    RewardResponse execute(UUID id, UpdateRewardRequest request);
}
