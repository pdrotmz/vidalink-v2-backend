package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardImageResponse;

import java.util.UUID;

public interface GetRewardImageUseCase {

    RewardImageResponse execute(UUID id);
}
