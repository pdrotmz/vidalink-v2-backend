package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;

import java.util.UUID;

public interface GetRewardByIdUseCase {

    RewardResponse execute(UUID id);
}
